package com.lokesh.poc.order.service.imp;

import com.lokesh.poc.order.dataobject.BagDO;
import com.lokesh.poc.order.dataobject.request.TransactionRequest;
import com.lokesh.poc.order.dataobject.response.CheckoutResponse;
import com.lokesh.poc.order.dataobject.response.TransactionResponse;
import com.lokesh.poc.order.dto.BagItemDto;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.exception.ClientNotAllowedException;
import com.lokesh.poc.order.exception.ItemNotFoundException;
import com.lokesh.poc.order.repository.OrderRepository;
import com.lokesh.poc.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WebClient.Builder webClientBuilder;

//    @Override
    public Mono<TransactionResponse> createOrder1(String bagId, TransactionRequest body) {

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

//    @Override
    public Mono<TransactionResponse> createOrder2(String bagId, TransactionRequest body) {

//        Mono<BagItemDto> bagItemDtoMono = bagClient.get().uri("/bags/{bagId}", bagId).retrieve().bodyToMono(BagItemDto.class);
//        bagItemDtoMono.subscribe(System.out::println);
//        log.info(String.valueOf(bagItemDtoMono));
        return webClientBuilder
                .baseUrl("http://localhost:8084/api/bagItem/v1")
                .build()
                .get()
                .uri("/bags/{bagId}", bagId)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(BagItemDto.class);
                    } else if (clientResponse.statusCode().is4xxClientError()) {
                        return ClientNotAllowedException.monoResponseClientNotAllowedException();
                    } else {
                        return Mono.empty();
                    }
                })
                .onErrorMap(ex -> new ClientNotAllowedException("Communication to client failed"))
                .switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(body.getOrder().getBagId(), ""))
                .flatMap(bagItemDto -> {
                    body.getPayment().setOrderId(body.getOrder().getOrderId());
//                    body.getPayment().setAmount(body.getBagItem().getQty() * bagItemDto.getPrice());
                    OrderDto orderDto = new OrderDto();
                    orderDto.setBagId(bagId);
                    orderDto.setStatus("created");
                    TransactionResponse response = new TransactionResponse();
                    response.setOrderDto(orderDto);
                    response.setAmount(body.getPayment().getAmount());
                    response.setTransactionId(body.getPayment().getTransactionId());
                    response.setMessage("Order created successfully");
                    return Mono.just(response);
                });
    }

    @Override
    public Mono<TransactionResponse> createOrder(String bagId, TransactionRequest body) {

        Flux<TransactionRequest> requestFlux = webClientBuilder
                .baseUrl("http://localhost:8084/api/bagItem/v1")
                .build()
                .get()
                .uri("/user-bag/{bagId}", bagId)
                .exchangeToFlux(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToFlux(BagItemDto.class);
                    } else if (clientResponse.statusCode().is4xxClientError()) {
                        return ClientNotAllowedException.fluxResponseClientNotAllowedException();
                    } else {
                        return Flux.empty();
                    }
                })
                .onErrorMap(ex -> new ClientNotAllowedException("Communication to client failed"))
                .switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(body.getOrder().getBagId(), ""))
                .map(bagItem -> {
//                    double amount = bagItem.getQty() * body.getItem().getPrice();
//                    body.getPayment().setOrderId(body.getOrder().getOrderId());
//                    body.getPayment().setAmount(amount);
                    return body;
                });

        Mono<TransactionResponse> transactionResponseMono = requestFlux.map(bagItemDto -> {
//            body.getPayment().setOrderId(body.getOrder().getOrderId());
//            body.getPayment().setAmount(body.getBagItem().getQty() * bagItemDto.getPrice());

            OrderDto orderDto = new OrderDto();
            orderDto.setBagId(bagId);
            orderDto.setStatus("created");

            TransactionResponse response = new TransactionResponse();
            response.setOrderDto(orderDto);
            response.setAmount(body.getPayment().getAmount());
            response.setTransactionId(body.getPayment().getTransactionId());
            response.setMessage("Order created successfully");

            return response;
        }).single();

        return transactionResponseMono.switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(body.getOrder().getBagId(), ""));
    }

    @Override
    public Mono<CheckoutResponse> getCheckoutSummary(String bagId) {
        return webClientBuilder
                .baseUrl("http://localhost:8084/api/bagItem/v1")
                .build()
                .get()
                .uri("/user-bag/{bagId}", bagId)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(BagDO.class);
                    } else if (clientResponse.statusCode().is4xxClientError()) {
                        return ClientNotAllowedException.monoResponseClientNotAllowedException();
                    } else {
                        return Mono.empty();
                    }
                })
                .onErrorMap(ex -> new ClientNotAllowedException("Communication to client failed"))
                .switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(bagId, ""))
//                .flatMapIterable(BagDO::getBagItem)//Mono<BagItemDO>
//                .map(bagItemDO -> {
//                    double totalAmountOfBagItems = 0;
//                    for (int i = 0; i < bagItemDO.getQty(); i++) {
//                        totalAmountOfBagItems += bagItemDO.getPrice();
//                    }
////                    return bagItemDO;
//                    return new CheckoutResponse(bagId, )
//                })
                .map(bagItem -> {
                    double totalAmountOfBagItems = 0;
                    for (int i = 0; i < bagItem.getBagItem().size(); i++) {
                        totalAmountOfBagItems += bagItem.getBagItem().get(i).getPrice();
                    }
                    return new CheckoutResponse(bagId, bagItem.getTotalItem(), bagItem.getBagItem(), totalAmountOfBagItems);
                });
    }
}
