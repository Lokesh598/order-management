package com.lokesh.poc.order.service.imp;

import com.lokesh.poc.order.dataobject.BagDO;
import com.lokesh.poc.order.dataobject.request.OrderRequest;
import com.lokesh.poc.order.dataobject.response.CheckoutResponse;
import com.lokesh.poc.order.dataobject.response.OrderResponse;
import com.lokesh.poc.order.dto.BagItemDto;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.dto.PaymentDto;
import com.lokesh.poc.order.dto.UserDto;
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

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    WebClient.Builder webClientBuilder;

    public String getUserInfo() {
        return userInfo;
    }

    private String userInfo;

    @Override
    public Mono<OrderResponse> createOrder(String bagId, String emailId, OrderRequest request) {

        Mono<UserDto> userDtoMono = webClientBuilder
                .baseUrl("http://localhost:8082/api/user/v1/")
                .build()
                .get()
                .uri("/login/{emailId}", emailId)
                .retrieve()
                .bodyToMono(UserDto.class)
                        .map(userDto -> {
                            setUserInfo(userDto.getUserId());
                            System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj  "+userDto.getUserId());
                            return userDto;
                        });
                userDtoMono.subscribe();

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
                //todo: item not found exception give item id instead of bagId
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

                    //paymentID
                    String paymentId = UUID.randomUUID().toString().substring(0,8);
                    paymentDto.setPaymentId(paymentId);
                    paymentDto.setUserId(request.getUserId());
                    paymentDto.setAmount(request.getAmount());

                    String orderId = UUID.randomUUID().toString().substring(0,8);
                    orderDto.setOrderId(orderId);
                    orderDto.setUserId(getUserInfo());
                    orderDto.setTransactionId(paymentDto.getPaymentId());
                    orderDto.setTrackingId(bagId);

                    double amountToBePaid = request.getAmount();
                    boolean status = false;
                    String message = null;
                    double paid = totalAmountOfBagItems - amountToBePaid;
                    if(paid == 0.0) {
                        status = true;
                        message = "Thank you for shop from us🤩🤩🤩";
                        paymentDto.setPaymentStatus("Success");
                        orderDto.setStatus("Success");
                    } else {
                        message = "Payment Fail";
                        paymentDto.setPaymentStatus("Fail");
                        orderDto.setStatus("Fail, Try again");
                    }


//                    body.getPaymentDto().setPaymentId(bagItem.getBagId());
                    PaymentEntity paymentEntity = new PaymentEntity();
//                    paymentEntity.setId(paymentDto.getId());
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

                    if (status) {
                        this.orderRepository.save(orderEntity).subscribe();
                        this.paymentRepository.save(paymentEntity).subscribe();
                    }
                    return new OrderResponse(amountToBePaid, status, message, bagId);
                });
    }

    private void setUserInfo(String userId) {
        this.userInfo = userId;
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

    @Override
    public Flux<OrderDto> getOrderSummary(String emailId) {
        Mono<UserDto> userDtoMono = webClientBuilder
                .baseUrl("http://localhost:8082/api/user/v1/")
                .build()
                .get()
                .uri("/login/{emailId}", emailId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .map(userDto -> {
                    setUserInfo(userDto.getUserId());
                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj  "+userDto.getUserId());
                    return userDto;
                });
        userDtoMono.subscribe();

        return this.orderRepository.findAllById(getUserInfo());
    }
}
