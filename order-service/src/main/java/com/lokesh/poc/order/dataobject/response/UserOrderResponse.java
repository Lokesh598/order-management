package com.lokesh.poc.order.dataobject.response;


import com.lokesh.poc.order.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class UserOrderResponse {
    private String id;
    private String userId;
    private String userName;
    private String emailId;
    private List<OrderDto> orders;

    public UserOrderResponse(String id, String userId, String userName, String emailId, List<OrderDto> order) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.emailId = emailId;
        this.orders = order;
    }
}
