package com.lokesh.poc.bag.utils;

import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.model.entity.BagItemEntity;
import org.springframework.beans.BeanUtils;

public class BagItemEntityDtoUtils {

    public static BagItemDto entityToDto(BagItemEntity bagProduct) {
        BagItemDto bagItemDto = new BagItemDto();
        BeanUtils.copyProperties(bagProduct, bagItemDto);
        return bagItemDto;
    }
    public static BagItemEntity dtoToEntity(BagItemDto bagItemDto) {
        BagItemEntity bagProduct = new BagItemEntity();
        BeanUtils.copyProperties(bagItemDto, bagProduct);
        return bagProduct;
    }
}
