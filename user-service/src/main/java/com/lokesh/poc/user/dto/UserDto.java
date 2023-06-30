package com.lokesh.poc.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
//    @NotNull(message = "ID is required")
    private String id;

//    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Username is required")
    private String userName;

    @Email(message = "Invalid email format")
    private String emailId;

//    @NotNull(message = "Created date is required")
    private LocalDate createdDate;
}
