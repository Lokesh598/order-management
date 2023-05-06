package com.lokesh.poc.product.service.impl;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.repository.ProductRepository;
import com.lokesh.poc.product.service.ProductService;
import com.lokesh.poc.product.utils.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(EntityDtoUtil:: dtoToEntity)
                .flatMap(productRepository::insert)
                .map(EntityDtoUtil::entityToDto);
    }
}
