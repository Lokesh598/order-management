package com.lokesh.poc.order.service;

import com.lokesh.poc.order.dataobject.request.TransactionRequest;
import com.lokesh.poc.order.dataobject.response.TransactionResponse;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<TransactionResponse> createOrder(String bagId ,TransactionRequest body);
}
