package com.lokesh.poc.order.repository;

import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.model.entity.OrderEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<OrderEntity, String> {
    @Query("{userId:?0}")
    Flux<OrderDto> findAllById(String userId);

    Flux<OrderEntity> findByUserId(String userId);
}
