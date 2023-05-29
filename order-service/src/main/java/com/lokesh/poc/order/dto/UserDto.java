package com.lokesh.poc.order.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String userId;
    private String userName;
    private String emailId;
    private LocalDate createdDate;
}
