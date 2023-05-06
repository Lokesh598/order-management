package com.lokesh.poc.bag.repository;

import com.lokesh.poc.bag.model.entity.BagProductEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagProductRepository extends ReactiveMongoRepository<BagProductEntity, String> {
}
