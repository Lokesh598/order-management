package com.lokesh.poc.bag.repository;

import com.lokesh.poc.bag.model.entity.BagEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagRepository extends ReactiveMongoRepository<BagEntity, String> {
}
