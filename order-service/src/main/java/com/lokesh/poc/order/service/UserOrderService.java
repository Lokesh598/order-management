package com.lokesh.poc.order.service;

import com.lokesh.poc.order.dataobject.response.UserOrderResponse;
import com.lokesh.poc.order.dto.UserOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserOrderService {
    Mono<List<UserOrderResponse>> joinUserAndOrder(String emailId);

    Flux<UserOrderResponse> joinAllUserAndOrder();
}
