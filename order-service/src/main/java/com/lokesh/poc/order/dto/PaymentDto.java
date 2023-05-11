package com.lokesh.poc.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private String id;
    //paymentId == id
    private String paymentId;
    private String userId;
    private String paymentStatus;
    private double amount;
}
