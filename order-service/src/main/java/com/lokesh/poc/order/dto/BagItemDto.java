package com.lokesh.poc.order.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BagItemDto {
    private String id;
    private String bagId;
//    private String itemId;
//    private int qty;
    private List<ItemDto> itemDto;
    private Date lastModified;
}
