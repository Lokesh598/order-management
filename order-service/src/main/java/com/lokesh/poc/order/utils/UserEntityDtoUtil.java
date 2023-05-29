package com.lokesh.poc.order.utils;

import com.lokesh.poc.order.dto.UserDto;
import com.lokesh.poc.order.model.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class UserEntityDtoUtil {
    public static UserDto entityToDto(UserEntity user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public static UserEntity dtoToEntity(UserDto userDto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }
}
