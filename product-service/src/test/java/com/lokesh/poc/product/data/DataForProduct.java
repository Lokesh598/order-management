package com.lokesh.poc.product.data;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.model.entity.ProductEntity;
import com.lokesh.poc.product.repository.ProductRepository;

import java.security.Principal;

public class DataForProduct {
    public static ProductEntity productRequest() {
        ProductEntity productEntity = ProductEntity.builder()
                .id("1")
                .itemId("123")
                .name("awe")
                .description("awe wer awe")
                .price(12.1)
                .category("x").build();
        return productEntity;
    }
    public static ProductDto productRequestDto() {
        ProductDto productDto = ProductDto.builder()
                .id("1")
                .itemId("123")
                .name("awe")
                .description("awe wer awe")
                .price(12.1)
                .category("x").build();
        return productDto;
    }

//    public static ProductEntity productResponse() {
//        return productRequest()
//    }
}
