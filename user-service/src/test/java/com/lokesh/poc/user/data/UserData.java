package com.lokesh.poc.user.data;

import com.lokesh.poc.user.dto.UserDto;

import java.time.LocalDate;

public class UserData {
    public static UserDto userRequest() {
        return UserDto.builder()
                .id("123")
                .userId("123")
                .userName("Amit")
                .emailId("amit@gmail.com")
                .createdDate(LocalDate.now())
                .build();
    }

}
