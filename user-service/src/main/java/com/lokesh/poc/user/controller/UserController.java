package com.lokesh.poc.user.controller;


import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.service.UserService;
import com.lokesh.poc.user.utils.EntityDtoUtil;
import org.modelmapper.ModelMapper;
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
    public ResponseEntity<Flux<UserDto>> getAllUsers() {
        ResponseEntity<Flux<UserDto>> response;
        Flux<UserDto> usersList;
        try {
            usersList = this.userService.findAllUsers();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        response = ResponseEntity.ok(usersList);
        return response;
    }
    @PostMapping(value = "/signup")
    public ResponseEntity<Mono<UserDto>> addUser(@RequestBody Mono<UserDto> userDto) {
        ResponseEntity<Mono<UserDto>> response;
        Mono<UserDto> userDtoMono;
        try {
            userDtoMono = this.userService.addNewUser(userDto);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
//        response = this.userService.addNewUser(userEntity);
        response = ResponseEntity.ok(userDtoMono);
        return response;
    }
}
