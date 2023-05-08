package com.lokesh.poc.bag.service.impl;

import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.repository.BagItemRepository;
import com.lokesh.poc.bag.repository.BagRepository;
import com.lokesh.poc.bag.service.BagItemService;
import com.lokesh.poc.bag.utils.BagItemEntityDtoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class BagItemServiceImpl implements BagItemService {

    @Autowired
    BagRepository bagRepository;
    @Autowired
    BagItemRepository bagItemRepository;
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
                    item.setQty(item.getQty());
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
    public Flux<BagItemDto> getBagByBagId(String bagId) {
        Flux<BagItemDto> bagItemDtoMono = this.bagItemRepository
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
