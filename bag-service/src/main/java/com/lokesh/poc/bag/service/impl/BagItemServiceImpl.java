package com.lokesh.poc.bag.service.impl;

import com.lokesh.poc.bag.dataobject.response.BagDO;
import com.lokesh.poc.bag.dataobject.response.BagItemDO;
import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.dto.ItemDto;
import com.lokesh.poc.bag.exception.ClientNotAllowedException;
import com.lokesh.poc.bag.exception.ItemNotFoundException;
import com.lokesh.poc.bag.model.entity.BagItemEntity;
import com.lokesh.poc.bag.repository.BagDORepository;
import com.lokesh.poc.bag.repository.BagItemRepository;
import com.lokesh.poc.bag.repository.BagRepository;
import com.lokesh.poc.bag.service.BagItemService;
import com.lokesh.poc.bag.utils.BagEntityDtoUtil;
import com.lokesh.poc.bag.utils.BagItemEntityDtoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
public class BagItemServiceImpl implements BagItemService {

    @Autowired
    BagRepository bagRepository;
    @Autowired
    BagItemRepository bagItemRepository;
    @Autowired
    BagDORepository bagDORepository;
    @Autowired
    WebClient.Builder webClientBuilder;
    @Override
    public Mono<BagItemDto> addProductToBag(Optional<String> userId, Mono<BagItemDto> bagProductDto) {

        //setting values for BagEntity
//        Mono<BagDto> bagDto;
//        bagDto.map(BagEntityDtoUtil::dtoToEntity)
//                .map( bag -> {
//                            bag.setUserId(bag.getUserId());
//                            bag.setCreated(new Date());
//                            bag.setLastModified(new Date());
//                            return bag;
//                })
//                .flatMap(bagRepository::insert)
//                .map(BagEntityDtoUtil::dtoToEntity);

        BagDto bagDto = new BagDto();

        bagDto.setUserId(bagDto.getUserId());
        bagDto.setCreated(LocalDate.now());
        bagDto.setLastModified(LocalDate.now());

        Mono<BagDto> bagDtoMono = Mono.just(bagDto);
        bagDtoMono.map(BagEntityDtoUtil::dtoToEntity).flatMap(bagRepository::insert);

        return bagProductDto
                .map(BagItemEntityDtoUtils::dtoToEntity)
                .map(bagItem -> {
                    bagItem.setLastModified(LocalDate.now());
                    return bagItem;
                })
                .flatMap(bagItemRepository::insert)
                .map(BagItemEntityDtoUtils::entityToDto);
    }

    @Override
    public Mono<Void> removeProductFromBag(String bagProductId) {
        return this.bagItemRepository
                .deleteById(bagProductId);
    }

    @Override
    public Mono<BagItemDto> addItemToBag(Mono<BagItemDto> bagItemDto) {
        return bagItemDto
                .map(BagItemEntityDtoUtils::dtoToEntity)
                .map(item -> {
                    item.setLastModified(LocalDate.now());
//                    item.setQty(item.getQty());
                    return item;
                })
                .flatMap(bagItemRepository::insert)
                .map(BagItemEntityDtoUtils::entityToDto);
    }

    @Override
    public Mono<BagItemDto> getBagByBagId(String bagId) {
        Mono<BagItemDto> bagItemDtoMono = this.bagItemRepository
                .findByBagId(bagId);
        bagItemDtoMono.subscribe(System.out::println);
        return bagItemDtoMono;
    }

    @Override
    public Mono<BagItemDto> retrieveBag(Mono<BagItemDto> bagItemDto) {
        return null;
    }

    /**
     * Bag summary method
     *
     * @param bagId
     * @return bag summary
     */

    @Override
    public Mono<BagDO> getUserBag(String bagId) {
        Mono<BagItemDto> bagItemDtoMono = this.bagItemRepository.findByBagId(bagId);
        final String[] itemId = new String[1];
        return bagItemDtoMono
                .map(BagItemDto::getItemDto) // extract list of ItemDto objects
                .flatMapIterable(Function.identity()) // flatten list into a Flux of ItemDto objects
                .flatMap(itemDto -> {
                    itemId[0] = itemDto.getItemId();
                    log.info(itemId[0]);
                    // fetch BagItemDO by making a request to the product API
                    Mono<BagItemDO> bagItemDOMono = webClientBuilder
                            .baseUrl("http://localhost:8081/api/product/v1")
                            .build()
                            .get()
                            .uri("/product/{itemId}", itemDto.getItemId())
                            .retrieve()
                            .bodyToMono(BagItemDO.class)
                            .map(item -> {
                                item.setQty(itemDto.getQty());
                                item.setPrice(itemDto.getQty()* item.getPrice());
                                return item;
                            });

                    bagItemDOMono.subscribe(s-> System.out.println(s));
                    return bagItemDOMono;
                })
                .onErrorMap(ex -> new ClientNotAllowedException("Communication to client failed"))
                // Todo: item not found exception
                .switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException( itemId[0] ,""))
                .collectList()
                .map(bagItemDOS -> {
                    int totalItem = bagItemDOS.size();
                    List<BagItemDO> bagItemDOList = bagItemDOS;
                    return new BagDO(bagId, totalItem, bagItemDOList);
                })
                .flatMap(bagDO -> {
                    bagDO.setId(bagDO.getBagId());
                    return this.bagDORepository.save(bagDO);
                });// to save bag summary in collection


    }

    /**
     * service consuming kafka topic
     * @Return: user bag summary
     */

    Mono<BagDO> getUserBagSummary(String bagId) {
        Mono<BagItemDto> bagItemDtoMono = this.bagItemRepository.findByBagId(bagId);
        return null;
    }
    /**
     *
     * @param bagId
     * @param bagItem
     * @return: updated bag
     */
    @Override
    public Mono<BagItemDto> updateItemsInBag(String bagId, ItemDto bagItem) {
        Mono<BagItemDto> bagItemDtoMono = this.bagItemRepository
                .findByBagId(bagId)
                .switchIfEmpty(Mono.error(new RuntimeException("Bag not found for id: " + bagId)));;
        bagItemDtoMono.subscribe(System.out::println);

        return bagItemDtoMono.flatMap(bagItemDto -> {
            List<ItemDto> itemList = bagItemDto.getItemDto();
            //todo : check item is already in bag if not then add
            itemList.add(bagItem);
            bagItemDto.setItemDto(itemList);
            BagItemEntity bagItemEntity = new BagItemEntity();
            bagItemEntity.setId(bagItemDto.getId());
            bagItemEntity.setBagId(bagItemDto.getBagId());
            bagItemEntity.setUserId(bagItemDto.getUserId());
            bagItemEntity.setItemDto(bagItemDto.getItemDto());
            bagItemEntity.setLastModified(bagItemDto.getLastModified());
            return this.bagItemRepository.save(bagItemEntity).map(BagItemEntityDtoUtils::entityToDto);
//            return this.bagItemRepository.save((BagItemEntity) bagItemDto);
        });
    }
}
