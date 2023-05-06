package com.lokesh.poc.bag.utils;

import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.dto.BagProductDto;
import com.lokesh.poc.bag.model.entity.BagEntity;
import com.lokesh.poc.bag.model.entity.BagProductEntity;
import org.springframework.beans.BeanUtils;

public class BagProductEntityDtoUtils {

    public static BagProductDto entityToDto(BagProductEntity bagProduct) {
        BagProductDto bagProductDto = new BagProductDto();
        BeanUtils.copyProperties(bagProduct, bagProductDto);
        return bagProductDto;
    }
    public static BagProductEntity dtoToEntity(BagProductDto bagProductDto) {
        BagProductEntity bagProduct = new BagProductEntity();
        BeanUtils.copyProperties(bagProductDto, bagProduct);
        return bagProduct;
    }
}
