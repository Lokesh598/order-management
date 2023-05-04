package com.lokesh.poc.user.service.impl;

import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.repository.UserRepository;
import com.lokesh.poc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public Flux<UserEntity> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public Mono<UserEntity> addNewUser(UserEntity userEntity) {
        return this.userRepository.save(userEntity);
    }
}
