package com.lokesh.poc.product.service;

import com.lokesh.poc.product.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono);

    Mono<ProductDto> getItemByItemId(String itemId);

    Flux<ProductDto> getAllItems();

    Mono<ProductDto> updateItemInfo(String id, Mono<ProductDto> productDto);

    Mono<ProductDto> updateOrInsertItem(String id, Mono<ProductDto> productDto);
}
