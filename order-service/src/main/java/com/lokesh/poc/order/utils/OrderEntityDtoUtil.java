package com.lokesh.poc.order.utils;

import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.model.entity.OrderEntity;
import org.springframework.beans.BeanUtils;

public class OrderEntityDtoUtil {
    public static OrderDto entityToDto(OrderEntity order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        return orderDto;
    }

    public static OrderEntity dtoToEntity(OrderDto orderDto) {
        OrderEntity order = new OrderEntity();
        BeanUtils.copyProperties(orderDto, order);
        return order;
    }
}
