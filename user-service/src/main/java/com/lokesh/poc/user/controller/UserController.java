package com.lokesh.poc.user.controller;


import com.lokesh.poc.user.dataobject.UserNameRequest;
import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.exception.UserNotFoundException;
import com.lokesh.poc.user.exception.UserWithEmailIdAlreadyExistException;
import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.service.UserService;
//import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


/**
 * .onErrorMap() commented because we dont need to handle exception from controller.
 * @Author: Lokesh Singh
 */
//@Api("user-service")
@Controller
@RestController
@RequestMapping(value = "/api/user/v1")
public class UserController {

    @Autowired
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public Flux<UserDto> getAllUsers() {
        return this.userService
                .findAllUsers();
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build())
//                .log();
    }
    @GetMapping(value = "/login/{emailId}")
    public Mono<UserDto> getUserByEmailId(@PathVariable("emailId") String emailId) {
        return this.userService
                .findUserByEmailId(emailId);
//                .map(ResponseEntity::ok)
//                .onErrorMap(ex -> new UserNotFoundException(ex.getMessage()))
//                .log();
    }
    @PostMapping(value = "/signup")
    public Mono<UserDto> addUser(@Valid @RequestBody Mono<UserDto> userDto) {
        return this.userService
                .addNewUser(userDto);
//                .map(ResponseEntity::ok)
//                .onErrorMap(DuplicateKeyException.class, ex -> new UserWithEmailIdAlreadyExistException("user already exist"))
//                .log();
    }
    //6/6/2023 todo: update and delete users
    //6/6/23 todo: done
    @PutMapping(value = "/update/{emailId}")
    public Mono<UserDto> updateUser(@PathVariable("emailId") String emailId, @Valid @RequestBody UserNameRequest userName) {
        return this.userService
                .updateUserInfo(emailId, userName);
//                .onErrorMap(ex -> new UserNotFoundException(ex.getMessage()));
//                .flatMap(user -> Mono.just(ResponseEntity.ok(user)))
//                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    @DeleteMapping("/delete/{emailId}")
    private Mono<ResponseEntity<String>> deleteUser(@PathVariable("emailId") String emailId) {
        //todo: status..handling
        return this.userService.deleteUserInfo(emailId)
                .flatMap(user -> Mono.just(ResponseEntity.ok("Deleted Successfully")))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
