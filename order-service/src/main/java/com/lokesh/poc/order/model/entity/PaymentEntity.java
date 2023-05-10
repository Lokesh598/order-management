package com.lokesh.poc.order.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor()
@Document(value = "payment")
public class PaymentEntity {
    private String paymentId;
    private String paymentStatus;
    private String transactionId;
    private String orderId;
    private double amount;
}
