package com.lokesh.poc.product.utils;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.model.entity.ProductEntity;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {
    public static ProductDto entityToDto(ProductEntity product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static ProductEntity dtoToEntity(ProductDto productDto) {
        ProductEntity product = new ProductEntity();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
