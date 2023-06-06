package com.lokesh.poc.user.service.impl;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.repository.UserRepository;
import com.lokesh.poc.user.utils.EntityDtoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
//import static reactor.core.publisher.Mono.when;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    private String TEST_EMAIL = "test@gmail.com";
    @Test
    void addNewUser() {
    }

    @Test
    void shouldFindUserByEmailId() {
        Mono<UserDto> userDto = Mono.just(UserDto.builder()
                .id("1")
                .userId("1")
                .userName("Lokesh")
                .emailId("test@gmail.com")
                .createdDate(LocalDate.now())
                .build());
        when(userRepository.findByEmailId(TEST_EMAIL)).thenReturn(userDto);
        Mono<UserDto> userMono = userService.findUserByEmailId(TEST_EMAIL);
        StepVerifier
                .create(userMono)
                .consumeNextWith(newUser -> {
                    assertEquals(newUser.getEmailId(), TEST_EMAIL);
                })
                .verifyComplete();

    }

    @Test
    void updateUserInfo() {
    }

    @Test
    void deleteUserInfo() {
    }
}