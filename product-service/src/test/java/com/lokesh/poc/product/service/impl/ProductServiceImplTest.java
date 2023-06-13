package com.lokesh.poc.product.service.impl;

import com.lokesh.poc.product.data.DataForProduct;
import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.exception.ProductNotFoundException;
import com.lokesh.poc.product.model.entity.ProductEntity;
import com.lokesh.poc.product.repository.ProductRepository;
import com.lokesh.poc.product.utils.EntityDtoUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void saveProduct() {
        ProductDto productDto = DataForProduct.productRequestDto();

        ProductEntity productEntity = EntityDtoUtil.dtoToEntity(productDto);
        productEntity.setId("1");

        when(productRepository.insert(productEntity)).thenReturn(Mono.just(productEntity));

        Mono<ProductDto> result = productService.saveProduct(Mono.just(productDto));

        // Verify the result using StepVerifier
        StepVerifier.create(result)
                .consumeNextWith(savedProduct -> {
                    assertEquals(productDto.getName(), savedProduct.getName());
                    assertEquals(productDto.getDescription(), savedProduct.getDescription());
                    assertEquals(productDto.getCategory(), savedProduct.getCategory());
                    assertEquals(productDto.getPrice(), savedProduct.getPrice());
                });
                //.verifyComplete();
    }

    @Test
    @DisplayName("Should return the product when the itemId exists")
    void getItemByItemId() {
        ProductDto productDto = DataForProduct.productRequestDto();
        String itemId = "123s2";
        ProductEntity productEntity = EntityDtoUtil.dtoToEntity(productDto);
        when(productRepository.findByItemId(itemId)).thenReturn(Mono.just(productDto));
       when(productRepository.findById(productDto.getId())).thenReturn(Mono.just(productEntity));

        Mono<ProductDto> result = productService.getItemByItemId(itemId);

        StepVerifier.create(result)
                .expectNextMatches(product -> productDto.getName().equals(product.getName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when the itemId does not exist")
    void getItemByItemIdWhenItemIdDoesNotExistThenThrowException() {
        String itemId = "1234567";
        when(productRepository.findByItemId(itemId)).thenReturn(Mono.empty());

        Mono<ProductDto> productDtoMono = productService.getItemByItemId(itemId);

        StepVerifier.create(productDtoMono)
                .expectError(ProductNotFoundException.class)
                .verify();
    }
}