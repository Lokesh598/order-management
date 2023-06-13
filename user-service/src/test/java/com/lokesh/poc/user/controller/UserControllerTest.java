package com.lokesh.poc.user.controller;

import com.lokesh.poc.user.dataobject.UserNameRequest;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

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

    // we dont want to interact with actual database, so we only mock service layer
    //@InjectMocks //it will not create actual object


    @Test
    public void addUserTest() {
        Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "amit", "amit@gmail.com", LocalDate.now()));

        when(userService.addNewUser(userDtoMono)).thenReturn(userDtoMono);

        webTestClient.post().uri("/api/user/v1/signup")
                .body(Mono.just(userDtoMono), UserDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }
    @Test
    public void getUserByEmailTest() {
        Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "amit", "amit@gmail.com", LocalDate.now()));
        when(userService.findUserByEmailId("amit@gmail.com")).thenReturn(userDtoMono);

        Flux<UserDto> responseBody = webTestClient.get().uri("/api/user/v1/login/amit@gmail.com")
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(p->p.getEmailId().equals("amit@gmail.com"))
                .verifyComplete();
    }

    @Test
    public void updateUserInfoTest() {
        Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "Lokesh", "lokesh@gmail.com", LocalDate.now()));
        when(userService.updateUserInfo("lokesh@gmail.com", new UserNameRequest("harish"))).thenReturn(userDtoMono);

        webTestClient.put().uri("/api/user/v1/update/lokesh@gmail.com")
                .body(Mono.just(userDtoMono),UserDto.class)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void deleteUserInfoTest() {
        Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "Lokesh", "lokesh@gmail.com", LocalDate.now()));
        when(userService.deleteUserInfo("lokesh@gmail.com")).thenReturn(userDtoMono);

        webTestClient.delete().uri("/api/user/v1/delete/lokesh@gmail.com")
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserDto.class)
                .getResponseBody();
    }
}