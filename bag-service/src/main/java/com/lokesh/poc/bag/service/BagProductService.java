package com.lokesh.poc.bag.service;

import com.lokesh.poc.bag.dto.BagProductDto;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface BagProductService {
    Mono<BagProductDto> addProductToBag(Mono<BagProductDto> bagProductDto);

    Mono<Void> removeProductFromBag(String bagProductId);
}
