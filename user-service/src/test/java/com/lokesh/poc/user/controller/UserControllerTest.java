package com.lokesh.poc.user.controller;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.exception.UserNotFoundException;
import com.lokesh.poc.user.exception.UserWithEmailIdAlreadyExistException;
import com.lokesh.poc.user.service.UserService;
import com.lokesh.poc.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void shouldGetUserByEmailId() {
        Mono<UserDto> userDto = Mono.just(UserDto.builder()
                .id("1")
                .userId("1")
                .userName("Lokesh")
                .emailId("test@gmail.com")
                .createdDate(LocalDate.now())
                .build());
        when(userService.findUserByEmailId("test@gmail.com")).thenReturn(userDto);

        webClient.get().uri("/api/user/v1/login/test@gmail.com")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDto.class);

    }

    @Test
    public void shouldGetUserFail() {
        when(userService.findUserByEmailId("test@gmail.com")).thenThrow(new UserNotFoundException("user not found"));
        webClient.get().uri("/api/user/v1/login/test@gmail.com")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody();
//                .jsonPath().isEqualTo();
    }
    @Test
    void addUser() {
        UserDto userDto = UserDto.builder()
                .id("1")
                .userId("1")
                .userName("Lokesh")
                .emailId("test@gmail.com")
                .createdDate(LocalDate.now())
                .build();
        when(userService.addNewUser(Mono.just(userDto))).thenReturn(Mono.just(userDto));
//        webClient.post().uri("/api/user/v1/signup")
//                .bodyValue(userDto)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(UserDto.class);

    }

    @Test
    void updateUser() {
    }
}