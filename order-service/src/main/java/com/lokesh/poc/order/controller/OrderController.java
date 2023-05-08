package com.lokesh.poc.order.controller;

import com.ctc.wstx.dtd.ModelNode;
import com.lokesh.poc.order.dataobject.request.TransactionRequest;
import com.lokesh.poc.order.dataobject.response.TransactionResponse;
import com.lokesh.poc.order.service.OrderService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RestController
@RequestMapping(value = "/api/order/v1")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("hello user");
    }
    @PostMapping(value = "/order/{bagId}")
    public Mono<TransactionResponse> doOrder(@PathVariable("bagId") String bagId, @RequestBody TransactionRequest body) {
        log.info("START: BagController :: bagId: {} ", bagId);
        return this.orderService.createOrder(bagId ,body);
    }
}
