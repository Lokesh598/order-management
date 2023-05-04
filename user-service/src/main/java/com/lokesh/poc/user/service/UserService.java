package com.lokesh.poc.user.service;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.model.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserDto> findAllUsers();

    Mono<UserDto> addNewUser(Mono<UserDto> userDtoMono);
}
