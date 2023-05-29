package com.lokesh.poc.order.service.imp;

import com.lokesh.poc.order.dataobject.response.UserOrderResponse;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.dto.UserOrderDto;
import com.lokesh.poc.order.repository.OrderRepository;
import com.lokesh.poc.order.repository.UserRepository;
import com.lokesh.poc.order.service.UserOrderService;
import com.lokesh.poc.order.utils.OrderEntityDtoUtil;
import com.lokesh.poc.order.utils.UserEntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class UserOrderServiceImpl implements UserOrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserOrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Mono<List<UserOrderResponse>> joinUserAndOrder(String emailId) {

        return userRepository.findByEmailId(emailId)
                .map(UserEntityDtoUtil::entityToDto)
                .flatMapMany(user ->
                        orderRepository.findByUserId(user.getUserId())
                                .map(OrderEntityDtoUtil::entityToDto)
                                .collectList()
                                .map(orders -> new UserOrderResponse(user.getId(), user.getUserId(), user.getUserName(), user.getEmailId(), orders)))
                .distinct(UserOrderResponse::getUserId)
                .collectList();
    }

    @Override
    public Flux<UserOrderResponse> joinAllUserAndOrder() {
        return userRepository.findAll()
                .map(UserEntityDtoUtil::entityToDto)
                .flatMap(user ->
                        orderRepository.findByUserId(user.getUserId())
                                .map(OrderEntityDtoUtil::entityToDto)
                                .collectList()
                                .map(orders -> new UserOrderResponse(user.getId(), user.getUserId(), user.getUserName(), user.getEmailId(), orders)))
                .distinct(UserOrderResponse::getUserId);
    }
}
