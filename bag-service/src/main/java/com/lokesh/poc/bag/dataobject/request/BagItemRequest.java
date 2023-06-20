package com.lokesh.poc.bag.dataobject.request;

import com.lokesh.poc.bag.dto.BagItemDto;
import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BagItemRequest {
    private String itemId;
    private int qty;
}
