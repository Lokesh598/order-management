package com.lokesh.poc.user.dto;


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
