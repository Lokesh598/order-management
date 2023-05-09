package com.lokesh.poc.order.controller;

import com.ctc.wstx.dtd.ModelNode;
import com.lokesh.poc.order.dataobject.request.TransactionRequest;
import com.lokesh.poc.order.dataobject.response.TransactionResponse;
import com.lokesh.poc.order.dto.BagItemDto;
import com.lokesh.poc.order.dto.UserDto;
import com.lokesh.poc.order.service.OrderService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RestController
@RequestMapping(value = "/api/order/v1")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    WebClient.Builder webclientBuilder;

    private WebClient bagClient = WebClient.create("http://localhost:8084/api/bagItem/v1");
    private WebClient userClient = WebClient.create("http://localhost:8082/api/user/v1");
    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("hello user");
    }
    @GetMapping("/getbag/{bagId}")
    public Flux<BagItemDto> getBag(@PathVariable String bagId) {
                Flux<BagItemDto> bagItemDtoMono = bagClient.get().uri("/bags/{bagId}", bagId).retrieve().bodyToFlux(BagItemDto.class);
        bagItemDtoMono.subscribe(System.out::println);
        log.info(String.valueOf(bagItemDtoMono));
        return bagItemDtoMono;
    }
    @GetMapping("/getUser/{userId}")
    public Mono<UserDto> getUser(@PathVariable String userId) {
        Mono<UserDto> userDtoMono = userClient.get().uri("/login/{userId}", userId).retrieve().bodyToMono(UserDto.class);
        userDtoMono.subscribe(System.out::println);
        return userDtoMono;
    }
    @PostMapping(value = "/order/{bagId}")
    public Mono<TransactionResponse> doOrder(@PathVariable("bagId") String bagId, @RequestBody TransactionRequest body) {
        log.info("START: BagController :: bagId: {} ", bagId);
        return this.orderService.createOrder(bagId ,body);
    }
}
