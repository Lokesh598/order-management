package com.lokesh.poc.user.controller;


import com.lokesh.poc.user.dataobject.UserNameRequest;
import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


/**
 * @Author: Lokesh Singh
 */
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
    }
    @GetMapping(value = "/login/{emailId}")
    public Mono<UserDto> getUserByEmailId(@PathVariable("emailId") String emailId) {
        return this.userService
                .findUserByEmailId(emailId);
    }
    @PostMapping(value = "/signup")
    public Mono<UserDto> addUser(@Valid @RequestBody Mono<UserDto> userDto) {
        return this.userService
                .addNewUser(userDto);
    }
    @PutMapping(value = "/update/{emailId}")
    public Mono<UserDto> updateUser(@PathVariable("emailId") String emailId, @Valid @RequestBody UserNameRequest userName) {
        return this.userService
                .updateUserInfo(emailId, userName);
    }
    @DeleteMapping("/delete/{emailId}")
    private Mono<ResponseEntity<String>> deleteUser(@PathVariable("emailId") String emailId) {
        //todo: status..handling
        return this.userService.deleteUserInfo(emailId)
                .flatMap(user -> Mono.just(ResponseEntity.ok("Deleted Successfully")))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
