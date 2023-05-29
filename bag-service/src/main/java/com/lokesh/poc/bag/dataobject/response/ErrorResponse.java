package com.lokesh.poc.bag.dataobject.response;

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
