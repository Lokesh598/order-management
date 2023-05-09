package com.lokesh.poc.product.repository;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.model.entity.ProductEntity;
import io.netty.util.AsyncMapping;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, String> {
    @Query("{itemId:?0}")
    Mono<ProductDto> findByItemId(String itemId);
}
