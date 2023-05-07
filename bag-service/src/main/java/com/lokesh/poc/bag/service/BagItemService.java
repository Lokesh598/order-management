package com.lokesh.poc.bag.service;

import com.lokesh.poc.bag.dto.BagItemDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface BagItemService {
    Mono<BagItemDto> addProductToBag(Mono<BagItemDto> bagProductDto);

    Mono<Void> removeProductFromBag(String bagProductId);

    Mono<BagItemDto> addItemToBag(Mono<BagItemDto> bagItemDto);
}
