package com.lokesh.poc.user.controller;


import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Flux<UserEntity>> getAllUsers() {
        ResponseEntity<Flux<UserEntity>> response;
        Flux<UserEntity> usersList;
        try {
            usersList = this.userService.findAllUsers();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        response = ResponseEntity.ok(usersList);
        return response;
    }
    @PostMapping(value = "/signup")
    public ResponseEntity<Mono<UserEntity>> addUser(@RequestBody UserEntity userEntity) {
        ResponseEntity<Mono<UserEntity>> response;
        Mono<UserEntity> userEntityMono;
        try {
            userEntityMono = this.userService.addNewUser(userEntity);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
//        response = this.userService.addNewUser(userEntity);
        response = ResponseEntity.ok(userEntityMono);
        return response;
    }
}
