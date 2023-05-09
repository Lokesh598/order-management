package com.lokesh.poc.bag.dto;

import com.fasterxml.jackson.core.JsonToken;
import lombok.*;
import org.springframework.web.service.annotation.GetExchange;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private String itemId;
    private int qty;
}
