package com.lokesh.poc.bag.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BagProductDto {
    private String id;
    private String bagId;
    private String itemId;
    private Date lastModified;
    private int quantity;
}
