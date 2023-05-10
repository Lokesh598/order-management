package com.lokesh.poc.order.dataobject.request;

import com.lokesh.poc.order.dto.BagItemDto;
import com.lokesh.poc.order.dto.OrderDto;
import com.lokesh.poc.order.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String bagId;
    private OrderDto order;
    private BagItemDto bagItem;
    private PaymentDto payment;
}
