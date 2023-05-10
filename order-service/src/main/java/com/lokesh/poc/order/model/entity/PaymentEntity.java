package com.lokesh.poc.order.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor()
@Document(value = "payment")
public class PaymentEntity {
    @Id
    private String id;
    private String paymentId;
    private String userId;
    private String paymentStatus;
    private double amount;
}
