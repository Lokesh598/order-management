package com.lokesh.poc.product.service.impl;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.exception.ProductNotFoundException;
import com.lokesh.poc.product.repository.ProductRepository;
import com.lokesh.poc.product.service.ProductService;
import com.lokesh.poc.product.utils.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(EntityDtoUtil:: dtoToEntity)
                .map(item ->  {
                    item.setItemId(UUID.randomUUID().toString().substring(0,7));
                    return item;
                })
                .flatMap(productRepository::insert)
                .map(EntityDtoUtil::entityToDto);
    }

    @Override
    public Mono<ProductDto> getItemByItemId(String itemId) {
        return this.productRepository
                .findByItemId(itemId)
                .switchIfEmpty(ProductNotFoundException.monoResponseProductNotFoundException(itemId, ""))
                .map(EntityDtoUtil::dtoToEntity)
                .map(item -> {
                    productRepository.findById(item.getId());
                    return item;
                })
                .map(EntityDtoUtil::entityToDto);
    }
}
