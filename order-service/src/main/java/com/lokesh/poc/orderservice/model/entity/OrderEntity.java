package com.lokesh.poc.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
@ToString
public class OrderEntity {
    @Id
    private String id;
    private String orderId;
    private String productId;
    private int qty;
    private String status;
}
