package com.lokesh.poc.order.dto;

import com.lokesh.poc.order.model.entity.OrderEntity;
import com.lokesh.poc.order.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserOrderDto {
        private UserDto user;

        public UserOrderDto(UserDto user) {
            this.user = user;
        }
}
