package com.lokesh.poc.user.utils;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.model.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {
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
