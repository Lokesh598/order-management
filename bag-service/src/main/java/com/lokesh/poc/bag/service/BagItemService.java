package com.lokesh.poc.bag.service;

import com.lokesh.poc.bag.dto.BagItemDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface BagItemService {
    Mono<BagItemDto> addProductToBag(Mono<BagItemDto> bagProductDto);

    Mono<Void> removeProductFromBag(String bagProductId);

    Mono<BagItemDto> addItemToBag(Mono<BagItemDto> bagItemDto);

    Flux<BagItemDto> getBagByBagId(String bagId);

    Mono<BagItemDto> retrieveBag(Mono<BagItemDto> bagItemDto);
}
