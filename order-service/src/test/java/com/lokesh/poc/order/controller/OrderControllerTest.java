package com.lokesh.poc.order.controller;

import com.lokesh.poc.order.dataobject.BagItemDO;
import com.lokesh.poc.order.dataobject.request.OrderRequest;
import com.lokesh.poc.order.dataobject.response.CheckoutResponse;
import com.lokesh.poc.order.dataobject.response.OrderResponse;
import com.lokesh.poc.order.dataobject.response.UserOrderResponse;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.dto.UserDto;
import com.lokesh.poc.order.repository.OrderRepository;
import com.lokesh.poc.order.service.OrderService;
import com.lokesh.poc.order.service.UserOrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @InjectMocks
    private OrderController orderController;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserOrderService userOrderService;

    @Test
    void testCheckoutSummary() {
        String bagId = "6470740e5ef328495e5b5888";
        CheckoutResponse checkoutResponse = new CheckoutResponse("bagId", 2, List.of(new BagItemDO("itemId", "itemName", 100000.00, 10)), 100000.00);
        when(orderService.getCheckoutSummary(bagId)).thenReturn(Mono.just(checkoutResponse));

        Mono<CheckoutResponse> checkoutSummary = orderController.checkoutSummary(bagId);

        StepVerifier.create(checkoutSummary)
                .expectNextCount(1)
                .verifyComplete();


    }

    @Test
    void testDoOrder() {
        String bagId = "6470740e5ef328495e5b5888";
        String emailId = "test@example.com";
        OrderRequest body = new OrderRequest("id", "paymentId", "userId", "True", 100000.00);
        OrderResponse orderResponse = new OrderResponse(100000.00, true, "message", "6470740e5ef328495e5b5888");

        when(orderService.createOrder(bagId, emailId ,body)).thenReturn(Mono.just(orderResponse));
        Mono<OrderResponse> orderResponseMono = orderController.doOrder(bagId, emailId, body);
        StepVerifier.create(orderResponseMono)
                .expectNextCount(1)
                .verifyComplete();

        verify(orderService, times(1)).createOrder(bagId, emailId, body);
    }
    @Test
    void testOrderSummary() {
        String emailId = "test@example.com";
        Flux<OrderDto> expectedOrders = Flux.just(
                new OrderDto("1", "Order1", "User1", "Transaction1", "TrackingId", "True"),
                new OrderDto("2", "Order2", "User2", "Transaction2", "TrackingId2", "True")
        );

        when(orderService.getOrderSummary(emailId)).thenReturn(expectedOrders);

        Flux<OrderDto> result = orderController.orderSummary(emailId);

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        verify(orderService, times(1)).getOrderSummary(emailId);

        webTestClient.get().uri("/api/order/v1/order-summary")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBodyList(OrderDto.class);
//        .value(order -> {
//            assertEquals("1", order.get(1).getId());
//            assertEquals("Order1", order.get(1).getOrderId());
//            assertEquals("User1", order.get(1).getUserId());
//            assertEquals("Transaction1", order.get(1).getTransactionId());
//            assertEquals("TrackingId", order.get(1).getTrackingId());
//            assertEquals("True", order.get(1).getStatus());
//        });
    }

    @Test
    void testJoinUserAndOrder() {
//        Mono<List<UserOrderResponse>>
        String emailId = "test@gmail.com";

        UserOrderResponse response1 = new UserOrderResponse("1", "userId1","Lokesh", "test@gmail.com", List.of(new OrderDto("1", "Order1", "userId1", "Transaction1", "TrackingId", "True")));
        UserOrderResponse response2 = new UserOrderResponse("2", "userId2","Lokendra", "test@gmail.com", List.of(new OrderDto("2", "Order2", "userId2", "Transaction2", "TrackingId2", "True")));
        List<UserOrderResponse> expectedResponse = Arrays.asList(response1, response2);

        when(userOrderService.joinUserAndOrder(emailId)).thenReturn(Mono.just(expectedResponse));

        Mono<List<UserOrderResponse>> result = orderController.joinUserAndOrder(emailId);

        StepVerifier.create(result)
                .expectNext(expectedResponse)
                .verifyComplete();

        verify(userOrderService, times(1)).joinUserAndOrder(emailId);
    }
    @Test
    void testJoinAllUserAndOrder() {
        UserOrderResponse userOrderResponse1 = new UserOrderResponse("1", "userId1","Lokesh", "test@gmail.com", List.of(new OrderDto("1", "Order1", "userId1", "Transaction1", "TrackingId", "True")));
        UserOrderResponse userOrderResponse2 = new UserOrderResponse("2", "userId2","Lokendra", "test@gmail.com", List.of(new OrderDto("2", "Order2", "userId2", "Transaction2", "TrackingId2", "True")));

        Flux<UserOrderResponse> userOrderResponseFlux = Flux.just(userOrderResponse1,userOrderResponse2);
        when(userOrderService.joinAllUserAndOrder()).thenReturn(userOrderResponseFlux);

        Flux<UserOrderResponse> result = orderController.joinAllUserAndOrder();
        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        verify(userOrderService, times(1)).joinAllUserAndOrder();

        assertNotNull(result);
    }
}
