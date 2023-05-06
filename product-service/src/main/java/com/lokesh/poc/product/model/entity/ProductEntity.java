package com.lokesh.poc.product.model.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "product")
@JsonRootName("product")
@JsonPropertyOrder("{}")
public class ProductEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
}
