package com.lokesh.poc.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
@ToString
public class OrderEntity {
    @Id
    private String orderId;
    private String bagId;
    private double amount;
    private String status;
}
