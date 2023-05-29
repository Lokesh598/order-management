package com.lokesh.poc.order.repository;

import com.lokesh.poc.order.model.entity.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity, String> {
    Mono<UserEntity> findByEmailId(String emailId);
}
