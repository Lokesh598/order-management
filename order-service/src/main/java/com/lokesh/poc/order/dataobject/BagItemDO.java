package com.lokesh.poc.order.dataobject;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BagItemDO {
    private String itemId;
    private String name;
    private double price;
    private int qty;
}
