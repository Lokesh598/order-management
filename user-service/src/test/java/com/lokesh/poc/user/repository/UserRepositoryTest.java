package com.lokesh.poc.user.repository;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.utils.EntityDtoUtil;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void shouldSaveSingleUserTest() {
        Mono<UserDto> userDto = Mono.just(UserDto.builder()
                .id("1")
                .userId("1")
                .userName("Lokesh")
                .emailId("lokesh@gmail.com")
                .createdDate(LocalDate.now())
                .build());
        Mono<UserEntity> user = userDto.map(EntityDtoUtil::dtoToEntity);
        Publisher<UserDto> setUp = user.flatMap(
                userEntity -> {
                    return userRepository.insert(userEntity);
                }
        ).map(user1-> (UserEntity)user1)
                .map(EntityDtoUtil::entityToDto);
        StepVerifier.create(setUp)
                .expectNextCount(0)
                .verifyComplete();
    }
}