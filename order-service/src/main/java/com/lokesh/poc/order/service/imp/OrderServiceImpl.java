package com.lokesh.poc.order.service.imp;

import com.lokesh.poc.order.dataobject.request.TransactionRequest;
import com.lokesh.poc.order.dataobject.response.TransactionResponse;
import com.lokesh.poc.order.dto.BagItemDto;
import com.lokesh.poc.order.dto.ItemDto;
import com.lokesh.poc.order.exception.ClientNotAllowedException;
import com.lokesh.poc.order.exception.ItemNotFoundException;
import com.lokesh.poc.order.repository.OrderRepository;
import com.lokesh.poc.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WebClient.Builder webClientBuilder;
    @Override
    public Mono<TransactionResponse> createOrder(String bagId, TransactionRequest body) {

        Mono<TransactionRequest> requestMono = webClientBuilder
                .baseUrl("http://localhost:8084/api/bagItem/v1")
                .build()
                .get()
                .uri("/bags/{bagId}", bagId)
                .exchangeToMono( clientResponse -> {
                    if( clientResponse.statusCode().equals(HttpStatus.OK) ) {
                        System.out.println(clientResponse.bodyToMono(BagItemDto.class));
                        return clientResponse.bodyToMono(BagItemDto.class);
                    } else if (clientResponse.statusCode().is4xxClientError()) {
                        return ClientNotAllowedException.monoResponseClientNotAllowedException();
                    } else {
                        return Mono.empty();
                    }
                })
                .onErrorMap( ex -> new ClientNotAllowedException("Communcation to client failed"))
                .switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(body.getOrder().getBagId(), ""))
                .map(i-> {
                    body.getPayment().setOrderId(body.getOrder().getOrderId());
//                    body.getPayment().setAmount(body.getBagItem().getQty()* i.getPrice());
                    return body;
                });
                requestMono.subscribe(System.out::println);
        return null;
    }
}
