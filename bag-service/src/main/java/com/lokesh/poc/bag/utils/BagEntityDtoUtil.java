package com.lokesh.poc.bag.utils;

import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.model.entity.BagEntity;
import org.springframework.beans.BeanUtils;

public class BagEntityDtoUtil {
    public static BagDto entityToDto(BagEntity bag) {
        BagDto bagDto = new BagDto();
        BeanUtils.copyProperties(bag, bagDto);
        return bagDto;
    }

    public static BagEntity dtoToEntity(BagDto bagDto) {
        BagEntity bag = new BagEntity();
        BeanUtils.copyProperties(bagDto, bag);
        return bag;
    }
}
