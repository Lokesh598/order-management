package com.lokesh.poc.order.dataobject.response;

import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.model.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private OrderDto orderDto;
    private double amount;
    private String transactionId;
    private String message;
}
