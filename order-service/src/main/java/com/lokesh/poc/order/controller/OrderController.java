package com.lokesh.poc.order.controller;

import com.lokesh.poc.order.dataobject.request.OrderRequest;
import com.lokesh.poc.order.dataobject.response.CheckoutResponse;
import com.lokesh.poc.order.dataobject.response.OrderResponse;
import com.lokesh.poc.order.dataobject.response.UserOrderResponse;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.dto.UserOrderDto;
import com.lokesh.poc.order.service.OrderService;
import com.lokesh.poc.order.service.UserOrderService;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Controller
@RestController
@RequestMapping(value = "/api/order/v1")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    UserOrderService userOrderService;

//    @Autowired
//    WebClient.Builder webclientBuilder;

    public OrderController(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

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

    //todo: join user and order collection based on userId

    @GetMapping("/join")
    public Mono<List<UserOrderResponse>> joinUserAndOrder(@RequestParam("emailId") String emailId) {
        return userOrderService.joinUserAndOrder(emailId);
    }

    // todo: return all user with their orders
    @GetMapping("/joinAllUsersOrder")
    public Flux<UserOrderResponse> joinAllUserAndOrder() {
        return userOrderService.joinAllUserAndOrder();
    }
}
