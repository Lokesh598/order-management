package com.lokesh.poc.order.controller;

import com.lokesh.poc.order.dataobject.request.OrderRequest;
import com.lokesh.poc.order.dataobject.response.CheckoutResponse;
import com.lokesh.poc.order.dataobject.response.OrderResponse;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.service.OrderService;
import jakarta.ws.rs.PathParam;
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

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("hello user");
    }


    @GetMapping(value = "/checkout-summary/{bagId}")
    public Mono<CheckoutResponse> checkoutSummary(@PathVariable String bagId) {
        log.info("START: OrderController :: checkoutSummary :: bagId: {}", bagId);
        return this.orderService.getCheckoutSummary(bagId);
    }
    @PostMapping(value = "/order/{bagId}")
    public Mono<OrderResponse> doOrder(@PathVariable("bagId") String bagId,
                                       @RequestParam(value = "userId" , required = false) String emailId,
                                       @RequestBody OrderRequest body) {
        log.info("START: BagController :: bagId: {} ", bagId);
        return this.orderService.createOrder(bagId, emailId ,body);
    }

    @GetMapping(value = "/order-summary")
    public Flux<OrderDto> orderSummary(@PathParam("emailId") String emailId) {
        return this.orderService.getOrderSummary(emailId);
    }
}
