package com.lokesh.poc.user.controller;


import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.exception.UserNotFoundException;
import com.lokesh.poc.user.exception.UserWithEmailIdAlreadyExistException;
import com.lokesh.poc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Api("user-service")
@Controller
@RestController
@RequestMapping(value = "api/user/v1")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/users")
    public Flux<ResponseEntity<UserDto>> getAllUsers() {
        return this.userService
                .findAllUsers()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .log();
    }
    @GetMapping(value = "/login/{emailId}")
    public Mono<ResponseEntity<UserDto>> getUserByEmailId(@PathVariable("emailId") String emailId) {
        return this.userService
                .findUserByEmailId(emailId)
                .map(ResponseEntity::ok)
                .onErrorMap(ex -> new UserNotFoundException(ex.getMessage()))
                .log();
    }
    @PostMapping(value = "/signup")
    public Mono<ResponseEntity<UserDto>> addUser(@RequestBody Mono<UserDto> userDto) {
        return this.userService
                .addNewUser(userDto)
                .map(ResponseEntity::ok)
                .onErrorMap(DuplicateKeyException.class, ex -> new UserWithEmailIdAlreadyExistException("user already exist"))
                .log();
    }
}
