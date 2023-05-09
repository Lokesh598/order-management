package com.lokesh.poc.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String itemId;
    private String name;
    private String description;

    private String category;
    private double price;
}
