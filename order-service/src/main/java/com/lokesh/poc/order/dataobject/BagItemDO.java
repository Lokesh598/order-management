package com.lokesh.poc.bag.dataobject.response;

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
