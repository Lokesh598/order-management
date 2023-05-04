package com.lokesh.poc.user.service.impl;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.repository.UserRepository;
import com.lokesh.poc.user.service.UserService;
import com.lokesh.poc.user.utils.EntityDtoUtil;
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
    public Flux<UserDto> findAllUsers() {
        return this.userRepository.findAll().map(EntityDtoUtil::entityToDto);
    }

    @Override
    public Mono<UserDto> addNewUser(Mono<UserDto> userDtoMono) {
//        return this.userRepository.save(userDtoMono);
        return userDtoMono
                .map(EntityDtoUtil::dtoToEntity)
                .flatMap(userRepository::insert)
                .map(EntityDtoUtil::entityToDto);
    }
}
