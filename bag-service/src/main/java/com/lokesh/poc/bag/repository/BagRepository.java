package com.lokesh.poc.bag.repository;

import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.model.entity.BagEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface BagRepository extends ReactiveMongoRepository<BagEntity, String> {
    @Query("{userId:?0}")
    Mono<BagDto> findByUserId(Optional<String> userId);
}
