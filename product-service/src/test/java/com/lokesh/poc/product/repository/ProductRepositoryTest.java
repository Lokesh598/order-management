package com.lokesh.poc.product.repository;

import com.lokesh.poc.product.data.DataForProduct;
import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.model.entity.ProductEntity;
import com.lokesh.poc.product.utils.EntityDtoUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByItemId() {
    }

    @Test
    public void shouldSaveItem() {
        String itemId = "123";

        ProductEntity productEntity = DataForProduct.productRequest();

        Publisher<ProductEntity> setUp = productRepository.deleteAll().then(productRepository.save(productEntity));
        Mono<ProductEntity> find = productRepository.findByItemId("123").map(EntityDtoUtil::dtoToEntity);

        Publisher<ProductEntity> composite = Mono.from(setUp).then(find);

        StepVerifier.create(composite).consumeNextWith(product -> {
            assertNotNull(productEntity.getId());
            assertEquals(productEntity.getItemId(), itemId);
            assertEquals(productEntity.getName(), "awe");
            assertEquals(productEntity.getPrice(), 12.1);
        }).verifyComplete();
    }

    @Test
    public void shouldFindItemByItemId() {
        ProductEntity productEntity = DataForProduct.productRequest();
        Publisher<ProductEntity> setUp = productRepository.deleteAll().then(productRepository.save(productEntity));

        Mono<ProductEntity> find = productRepository.findByItemId("123").map(EntityDtoUtil::dtoToEntity);

        Publisher<ProductEntity> composite = Mono.from(setUp).then(find);

        StepVerifier.create(composite).expectNextCount(1).verifyComplete();
    }
}