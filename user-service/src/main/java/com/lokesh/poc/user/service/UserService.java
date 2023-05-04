package com.lokesh.poc.user.service;

import com.lokesh.poc.user.model.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserEntity> findAllUsers();

    Mono<UserEntity> addNewUser(UserEntity userEntity);
}
