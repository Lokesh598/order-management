package com.lokesh.poc.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private String paymentId;
    private String paymentStatus;
    private String transactionId;
    private String orderId;
    private double amount;
}
