package com.lokesh.poc.user.service.impl;

import com.lokesh.poc.user.dataobject.UserNameRequest;
import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.exception.UserNotFoundException;
import com.lokesh.poc.user.exception.UserWithEmailIdAlreadyExistException;
import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.repository.UserRepository;
import com.lokesh.poc.user.service.UserService;
import com.lokesh.poc.user.utils.EntityDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public Flux<UserDto> findAllUsers() {
        log.info("find all users called");
        return this.userRepository.findAll().map(EntityDtoUtil::entityToDto);
    }

    @Override
//    public Mono<UserDto> addNewUser(Mono<UserDto> userDtoMono) {
//        return userDtoMono
//                .map(EntityDtoUtil::dtoToEntity)
//                .map(user -> {
//                    user.setUserId(UUID.randomUUID().toString());
//                    user.setCreatedDate(LocalDate.now());
//                    return user;
//                })
//                .switchIfEmpty(UserWithEmailIdAlreadyExistException.monoResponseUserWithEmailIdAlreadyExistException(
//
//                ))
//                .flatMap(userRepository::insert)
//                .map(EntityDtoUtil::entityToDto);
//    }
//    @AssertTrue
    public Mono<UserDto> addNewUser(Mono<UserDto> userDtoMono) {
        return userDtoMono
                .map(EntityDtoUtil::dtoToEntity)
                .map(user -> {
                    user.setUserId(UUID.randomUUID().toString().substring(0,7));
                    user.setCreatedDate(LocalDate.now());
                    return user;
                })
                .flatMap(user -> {
                    return userRepository.findByEmailId(user.getEmailId())
                            .flatMap(existingUser -> {
                                return Mono.error(new UserWithEmailIdAlreadyExistException("User with email ID already exists"));
                            })
                            .switchIfEmpty(userRepository.insert(user));
                })
                .map(user-> (UserEntity)user)
                .map(EntityDtoUtil::entityToDto);
    }


    @Override
    public Mono<UserDto> findUserByEmailId(String emailId) {
        log.info("Requesting by user with 👉👉" + emailId);
        return this.userRepository
                .findByEmailId(emailId)
                .switchIfEmpty(UserNotFoundException.monoResponseProductNotFoundException(emailId, null))
                .map(EntityDtoUtil::dtoToEntity)
                .map( user -> {
                    userRepository.findById(user.getId());
                    log.info(user.getUserName());
                    return user;
                }).map(EntityDtoUtil::entityToDto);
    }
    @Override
    public Mono<UserDto> updateUserInfo(String emailId, UserNameRequest userName) {
        return this.userRepository.findByEmailId(emailId)
                .map(EntityDtoUtil::dtoToEntity)
                .flatMap(user -> {
                    user.setUserName(userName.getUserName());
                    return save(user);
                }).switchIfEmpty(Mono.empty()).map(EntityDtoUtil::entityToDto);
    }
    private Mono<UserEntity> save(UserEntity user) {
        return this.userRepository.save(user);
    }
    @Override
    public Mono<UserDto> deleteUserInfo(String emailId) {
        return this.userRepository
                .findByEmailId(emailId).flatMap(u ->
                        this.userRepository
                                .deleteById(u.getId())
                                .thenReturn(u));
    }
}
