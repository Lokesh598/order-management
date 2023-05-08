package com.lokesh.poc.order.repository;

import com.lokesh.poc.order.model.entity.OrderEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<OrderEntity, String> {
}
