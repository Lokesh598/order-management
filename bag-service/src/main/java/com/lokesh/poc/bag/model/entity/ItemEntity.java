package com.lokesh.poc.bag.model.entity;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    private String itemId;
    private int qty;
}
