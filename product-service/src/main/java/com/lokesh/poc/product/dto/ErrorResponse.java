package com.lokesh.poc.product.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
}
