package com.lokesh.poc.bag.service.impl;

import com.lokesh.poc.bag.dataobject.response.BagDO;
import com.lokesh.poc.bag.dataobject.response.BagItemDO;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.repository.BagItemRepository;
import com.lokesh.poc.bag.repository.BagRepository;
import com.lokesh.poc.bag.service.BagItemService;
import com.lokesh.poc.bag.utils.BagItemEntityDtoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class BagItemServiceImpl implements BagItemService {

    @Autowired
    BagRepository bagRepository;
    @Autowired
    BagItemRepository bagItemRepository;

    @Autowired
    WebClient.Builder webClientBuilder;
    @Override
    public Mono<BagItemDto> addProductToBag(Mono<BagItemDto> bagProductDto) {
        return bagProductDto
                .map(BagItemEntityDtoUtils::dtoToEntity)
                .map(bagItem -> {
                    bagItem.setLastModified(new Date());
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
                    item.setLastModified(new Date());
//                    item.setQty(item.getQty());
                    return item;
                })
                .flatMap(bagItemRepository::insert)
                .map(BagItemEntityDtoUtils::entityToDto);

        //this will work for update bag
//        return this.bagItemRepository
//                .findById(bagId)
//                .doOnNext(e->e.setBagId(e.getBagId()))
//                .doOnNext(e->e.setItemId(e.getItemId()))
//                .map(bagItem -> {
//                    bagItem.setLastModified(new Date());
//                    return bagItem;
//                })
//                .flatMap(bagItemRepository::insert)
//                .map(BagItemEntityDtoUtils::entityToDto)
//                .log();
    }

    @Override
    public Mono<BagItemDto> getBagByBagId(String bagId) {
        Mono<BagItemDto> bagItemDtoMono = this.bagItemRepository
                .findByBagId(bagId);
        bagItemDtoMono.subscribe(System.out::println);
        return bagItemDtoMono;

//                .flatMap(bag -> this.bagItemRepository
//                        .findById(bag.getId())
//                        .thenReturn(bag));
    }

    @Override
    public Mono<BagItemDto> retrieveBag(Mono<BagItemDto> bagItemDto) {
        return null;
    }

    @Override
    public Mono<BagDO> getUserBag(String bagId) {
        Mono<BagItemDto> bagItemDtoMono = this.bagItemRepository.findByBagId(bagId);

        return bagItemDtoMono
                .map(BagItemDto::getItemDto) // extract list of ItemDto objects
                .flatMapIterable(Function.identity()) // flatten list into a Flux of ItemDto objects
                .flatMap(itemDto -> {
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
                .collectList()
                .map(bagItemDOS -> {
                    int totalItem = bagItemDOS.size();
                    List<BagItemDO> bagItemDOList = bagItemDOS;
                    return new BagDO(bagId, totalItem, bagItemDOList);
                });//Mono<BagDO>


//                .zipWith(bagItemDtoMono)
//                .map(tuple -> {
//                    // create BagDO object by combining BagItemDto and list of BagItemDO objects
//                    List<BagItemDO> bagItemDOList = Collections.singletonList((BagItemDO) tuple.getT1());
//                    BagItemDto bagItemDto = tuple.getT2();
//                    return new BagDO(bagItemDto.getBagId(), bagItemDOList);
//                });


//        Mono<BagItemDto> bagItemDto = this.bagItemRepository
//                .findByBagId(bagId);
//        bagItemDto.subscribe(System.out::println);
//
//        Mono<BagDO> bagItemDOList = bagItemDto.flatMapMany(bagItem ->
//                Flux.fromIterable(bagItem.getItemDto()))
//                .flatMap(itemDto -> {
//                    String itemId = itemDto.getItemId();
//                    Mono<BagItemDO> bagItemDO = webClientBuilder
//                            .baseUrl("http://localhost:8081/api/product/v1")
//                            .build()
//                            .get()
//                            .uri("/product/{itemId}", itemId)
//                            .retrieve()
//                            .bodyToMono(BagItemDO.class);
//                    return bagItemDO;
//                }).collectList().map(bagItemDOs -> {
//                    int totalItem = bagItemDOs.size();
//                    List<BagItemDO> bagItem = bagItemDOs;
//                    return new BagDO(bagId, totalItem, bagItem);
//                }
    }

//    @Override
//    public Mono<BagItemDto> retrieveBag(Mono<BagItemDto> bagItemDto) {
//        log.info("Retrieve(..) called", String.valueOf(bagItemDto));
//        return bagItemDto.flatMap(bagDto ->
//                this.bagItemRepository
//                        .findBagByBagId(bagDto.getBagId())
//                        .flatMap(bag -> this.bagItemRepository.findById(bagDto.getId())).flatMap(BagItemEntityDtoUtils::entityToDto)
//        );
//    }
}
