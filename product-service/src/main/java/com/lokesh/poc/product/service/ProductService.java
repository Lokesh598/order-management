package com.lokesh.poc.product.service;

import com.lokesh.poc.product.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ProductService {
   Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono);
}
