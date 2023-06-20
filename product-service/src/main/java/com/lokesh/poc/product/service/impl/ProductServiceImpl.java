package com.lokesh.poc.product.service.impl;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.exception.ProductNotFoundException;
import com.lokesh.poc.product.repository.ProductRepository;
import com.lokesh.poc.product.service.ProductService;
import com.lokesh.poc.product.utils.EntityDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
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

    @Override
    public Flux<ProductDto> getAllItems() {
        return this.productRepository.findAll().map(EntityDtoUtil::entityToDto);
    }

    @Override
    public Mono<ProductDto> updateItemInfo(String id, Mono<ProductDto> productDto) {

        return productRepository.findById(id)
                .flatMap(p->productDto.map(EntityDtoUtil::dtoToEntity)
                        .doOnNext(e->e.setId(id)))
                .flatMap(productRepository::save)
                .map(EntityDtoUtil::entityToDto);
    }
    @Override
    public Mono<ProductDto> updateOrInsertItem(String id, Mono<ProductDto> productDto) {
        Mono<ProductDto> product = this.productRepository.findById(id).map(EntityDtoUtil::entityToDto);

        boolean res = Boolean.TRUE.equals(product.hasElement().block());

        if (res) {
            return productRepository.findById(id)
                    .flatMap(p->productDto.map(EntityDtoUtil::dtoToEntity)
                            .doOnNext(e->e.setId(id)))
                    .flatMap(productRepository::save)
                    .map(EntityDtoUtil::entityToDto);
        } else {
            return productDto
                    .map(EntityDtoUtil:: dtoToEntity)
                    .map(item ->  {
                        item.setItemId(UUID.randomUUID().toString().substring(0,7));
                        return item;
                    })
                    .flatMap(productRepository::insert)
                    .map(EntityDtoUtil::entityToDto);
        }
    }
}
