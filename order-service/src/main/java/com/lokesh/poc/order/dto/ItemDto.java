package com.lokesh.poc.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
//    private String id;
//    private String name;
//    private String description;
//    private double price;
    private String itemId;
    private int qty;
}
