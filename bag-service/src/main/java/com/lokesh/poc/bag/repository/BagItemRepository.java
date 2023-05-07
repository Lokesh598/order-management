package com.lokesh.poc.bag.repository;

import com.lokesh.poc.bag.model.entity.BagItemEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagItemRepository extends ReactiveMongoRepository<BagItemEntity, String> {
}
