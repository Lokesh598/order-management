package com.lokesh.poc.order.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
@ToString
public class OrderEntity {
    @Id
    private String id;
    private String orderId;
    private String userId;
    private String transactionId;
    private String trackingId;
    private String status;
}
