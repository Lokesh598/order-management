package com.lokesh.poc.user.service;

import com.lokesh.poc.user.dataobject.UserNameRequest;
import com.lokesh.poc.user.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserDto> findAllUsers();

    Mono<UserDto> addNewUser(Mono<UserDto> userDtoMono);

    Mono<UserDto> findUserByEmailId(String emailId);

    Mono<UserDto> updateUserInfo(String emailId, UserNameRequest userName);

    Mono<UserDto> deleteUserInfo(String emailId);
}
