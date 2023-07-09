package com.lokesh.poc.order.service;

import com.lokesh.poc.order.dataobject.request.OrderRequest;
import com.lokesh.poc.order.dataobject.response.CheckoutResponse;
import com.lokesh.poc.order.dataobject.response.OrderResponse;
import com.lokesh.poc.order.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponse> createOrder(String bagId, String emailId , OrderRequest body);

    Mono<CheckoutResponse> getCheckoutSummary(String bagId);

    Flux<OrderDto> getOrderSummary(String emailId);
}
