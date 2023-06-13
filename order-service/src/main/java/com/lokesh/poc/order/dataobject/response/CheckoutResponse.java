package com.lokesh.poc.order.dataobject.response;

import com.lokesh.poc.order.dataobject.BagItemDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
    private String bagId;
    private int totalItem;
    private List<BagItemDO> bagItem;
    private double totalAmountOfBagItems;

}
