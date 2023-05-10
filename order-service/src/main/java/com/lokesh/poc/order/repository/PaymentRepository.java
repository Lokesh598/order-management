package com.lokesh.poc.order.repository;

import com.lokesh.poc.order.model.entity.PaymentEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends ReactiveMongoRepository<PaymentEntity, String> {
}
