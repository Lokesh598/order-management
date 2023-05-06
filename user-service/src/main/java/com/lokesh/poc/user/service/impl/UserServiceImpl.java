package com.lokesh.poc.user.service.impl;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.exception.UserNotFoundException;
import com.lokesh.poc.user.exception.UserWithEmailIdAlreadyExistException;
import com.lokesh.poc.user.repository.UserRepository;
import com.lokesh.poc.user.service.UserService;
import com.lokesh.poc.user.utils.EntityDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Slf4j
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
        return userDtoMono
                .map(EntityDtoUtil::dtoToEntity)
                .map(user -> {
                    user.setUserId(UUID.randomUUID().toString());
                    user.setCreatedDate(LocalDate.now());
                    return user;
                })
                .flatMap(userRepository::insert)
                .map(EntityDtoUtil::entityToDto);
    }

    @Override
    public Mono<UserDto> findUserByEmailId(String emailId) {
        log.info("Requesting by user with 👉👉" + emailId);
        return this.userRepository
                .findByEmailId(emailId)
                .switchIfEmpty(UserNotFoundException.monoResponseProductNotFoundException(emailId, null))
                .map(EntityDtoUtil::dtoToEntity)
                .map( userDto -> {
                    userRepository.findById(userDto.getId());
                    log.info(userDto.getUserName());
                    return userDto;
                }).map(EntityDtoUtil::entityToDto);
    }
}
