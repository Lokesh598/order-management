package com.lokesh.poc.order.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BagItemDto {
    private String id;
    private String bagId;
    private String itemId;
    private int qty;
    private Date lastModified;
}
