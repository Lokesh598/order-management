package com.lokesh.poc.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BagItemDto {
    private String id;
    private String bagId;
    private String itemId;
    private int qty;
    private Date lastModified;
}
