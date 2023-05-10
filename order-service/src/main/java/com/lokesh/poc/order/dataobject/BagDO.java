package com.lokesh.poc.order.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BagDO {
    private String bagId;
    private int totalItem;
    private List<BagItemDO> bagItem;
}
