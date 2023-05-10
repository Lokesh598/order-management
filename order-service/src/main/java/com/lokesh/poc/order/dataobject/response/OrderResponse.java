package com.lokesh.poc.order.dataobject.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    //user name
    //user address
    private double payment;
    private boolean status;
    private String message;
    private String trackingId;
}
