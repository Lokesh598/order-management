package com.lokesh.poc.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String id;
    private String orderId;
    private String userId;
    private String transactionId;
    private String trackingId;
    private String status;
}
