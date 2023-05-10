package com.lokesh.poc.order.service.imp;

import com.lokesh.poc.order.dataobject.BagDO;
import com.lokesh.poc.order.dataobject.request.OrderRequest;
import com.lokesh.poc.order.dataobject.response.CheckoutResponse;
import com.lokesh.poc.order.dataobject.response.OrderResponse;
import com.lokesh.poc.order.dto.BagItemDto;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.dto.PaymentDto;
import com.lokesh.poc.order.exception.ClientNotAllowedException;
import com.lokesh.poc.order.exception.ItemNotFoundException;
import com.lokesh.poc.order.model.entity.OrderEntity;
import com.lokesh.poc.order.model.entity.PaymentEntity;
import com.lokesh.poc.order.repository.OrderRepository;
import com.lokesh.poc.order.repository.PaymentRepository;
import com.lokesh.poc.order.service.OrderService;
import com.lokesh.poc.order.utils.OrderEntityDtoUtil;
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
    PaymentRepository paymentRepository;
    @Autowired
    WebClient.Builder webClientBuilder;

//    @Override
//    public Mono<OrderResponse> createOrder1(String bagId, OrderRequest body) {
//
//        Mono<OrderRequest> requestMono = webClientBuilder
//                .baseUrl("http://localhost:8084/api/bagItem/v1")
//                .build()
//                .get()
//                .uri("/bags/{bagId}", bagId)
//                .exchangeToMono( clientResponse -> {
//                    if( clientResponse.statusCode().equals(HttpStatus.OK) ) {
//                        System.out.println(clientResponse.bodyToMono(BagItemDto.class));
//                        return clientResponse.bodyToMono(BagItemDto.class);
//                    } else if (clientResponse.statusCode().is4xxClientError()) {
//                        return ClientNotAllowedException.monoResponseClientNotAllowedException();
//                    } else {
//                        return Mono.empty();
//                    }
//                })
//                .onErrorMap( ex -> new ClientNotAllowedException("Communcation to client failed"))
//                .switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(body.getOrder().getBagId(), ""))
//                .map(i-> {
//                    body.getPayment().setOrderId(body.getOrder().getOrderId());
////                    body.getPayment().setAmount(body.getBagItem().getQty()* i.getPrice());
//                    return body;
//                });
//                requestMono.subscribe(System.out::println);
//        return null;
//    }

//    @Override
//    public Mono<OrderResponse> createOrder2(String bagId, OrderRequest body) {

//        Mono<BagItemDto> bagItemDtoMono = bagClient.get().uri("/bags/{bagId}", bagId).retrieve().bodyToMono(BagItemDto.class);
//        bagItemDtoMono.subscribe(System.out::println);
//        log.info(String.valueOf(bagItemDtoMono));
//        return webClientBuilder
//                .baseUrl("http://localhost:8084/api/bagItem/v1")
//                .build()
//                .get()
//                .uri("/bags/{bagId}", bagId)
//                .exchangeToMono(clientResponse -> {
//                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
//                        return clientResponse.bodyToMono(BagItemDto.class);
//                    } else if (clientResponse.statusCode().is4xxClientError()) {
//                        return ClientNotAllowedException.monoResponseClientNotAllowedException();
//                    } else {
//                        return Mono.empty();
//                    }
//                })
//                .onErrorMap(ex -> new ClientNotAllowedException("Communication to client failed"))
//                .switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(body.getOrder().getBagId(), ""))
//                .flatMap(bagItemDto -> {
//                    body.getPayment().setOrderId(body.getOrder().getOrderId());
////                    body.getPayment().setAmount(body.getBagItem().getQty() * bagItemDto.getPrice());
//                    OrderDto orderDto = new OrderDto();
//                    orderDto.setBagId(bagId);
//                    orderDto.setStatus("created");
//                    OrderResponse response = new OrderResponse();
//                    response.setOrderDto(orderDto);
//                    response.setAmount(body.getPayment().getAmount());
//                    response.setTransactionId(body.getPayment().getTransactionId());
//                    response.setMessage("Order created successfully");
//                    return Mono.just(response);
//                });
//    }

    @Override
    public Mono<OrderResponse> createOrder(String bagId, OrderRequest body) {
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
                .map(bagItem -> {
                    double totalAmountOfBagItems = 0;
                    for (int i = 0; i < bagItem.getBagItem().size(); i++) {
                        totalAmountOfBagItems += bagItem.getBagItem().get(i).getPrice();
                    }
                    /**
                     * order response
                     *     private double payment;
                     *     private String status;
                     *     private String trackingId==bagId, for now;
                     */
                    PaymentDto paymentDto = new PaymentDto();
                    OrderDto orderDto = new OrderDto();

                    orderDto.setOrderId(orderDto.getOrderId());
                    orderDto.setUserId(null);
                    orderDto.setTransactionId(body.getPaymentDto().getId());
                    orderDto.setTrackingId(bagId);

                    paymentDto.setAmount(body.getPaymentDto().getAmount());
                    double amountToBePaid = body.getPaymentDto().getAmount();
                    boolean status = false;
                    String message = null;
                    double paid = totalAmountOfBagItems - amountToBePaid;
                    if(paid == 0.0) {
                        status = true;
                        message = "Thank you for shop from us🤩🤩🤩";
                        body.getPaymentDto().setPaymentStatus("Success");
                        orderDto.setStatus("Success");
                    } else {
                        message = "Payment Fail";
                        body.getPaymentDto().setPaymentStatus("Fail");
                        orderDto.setStatus("Fail, Try again");
                    }


                    body.getPaymentDto().setPaymentId(bagItem.getBagId());
                    PaymentEntity paymentEntity = new PaymentEntity();
                    paymentEntity.setId(paymentDto.getId());
                    paymentEntity.setPaymentId(paymentDto.getPaymentId());
                    paymentEntity.setUserId(paymentDto.getUserId());
                    paymentEntity.setPaymentStatus(paymentDto.getPaymentStatus());
                    paymentEntity.setAmount(paymentDto.getAmount());

                    //save order and payment in payment entity
                    OrderEntity orderEntity = new OrderEntity();
                    orderEntity.setOrderId(orderDto.getOrderId());
                    orderEntity.setUserId(orderDto.getUserId());
                    orderEntity.setTransactionId(orderDto.getTransactionId());
                    orderEntity.setTrackingId(orderDto.getTrackingId());
                    orderEntity.setStatus(orderDto.getStatus());

                    this.orderRepository.save(orderEntity).subscribe();
                    this.paymentRepository.save(paymentEntity).subscribe();
                    return new OrderResponse(amountToBePaid, status, message, bagId);
                });

//        Mono<OrderResponse> transactionResponseMono = requestFlux.map(bagItemDto -> {
////            body.getPayment().setOrderId(body.getOrder().getOrderId());
////            body.getPayment().setAmount(body.getBagItem().getQty() * bagItemDto.getPrice());
//
//            OrderDto orderDto = new OrderDto();
//            orderDto.setBagId(bagId);
//            orderDto.setStatus("created");
//
//            OrderResponse response = new OrderResponse();
//            response.setOrderDto(orderDto);
//            response.setAmount(body.getPayment().getAmount());
//            response.setTransactionId(body.getPayment().getTransactionId());
//            response.setMessage("Order created successfully");
//
//            return response;
//        }).single();
//
//        return transactionResponseMono.switchIfEmpty(ItemNotFoundException.monoResponseItemNotFoundException(bagId, ""));
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
