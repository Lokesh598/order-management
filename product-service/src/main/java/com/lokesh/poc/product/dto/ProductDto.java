package com.lokesh.poc.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String itemId;
    @NotBlank(message = "Item Name shouldn't be null or empty")
    private String name;
    @NotBlank(message = "Item description shouldn't be empty")
    private String description;
    @NotBlank(message = "Provide Item category ")
    private String category;
    @DecimalMin(value = "1.0", message = "Please Enter a valid Price")
    private double price;
}
