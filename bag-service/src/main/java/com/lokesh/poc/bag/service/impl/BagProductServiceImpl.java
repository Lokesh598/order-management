package com.lokesh.poc.bag.service.impl;

import com.lokesh.poc.bag.dto.BagProductDto;
import com.lokesh.poc.bag.repository.BagProductRepository;
import com.lokesh.poc.bag.service.BagProductService;
import com.lokesh.poc.bag.utils.BagEntityDtoUtil;
import com.lokesh.poc.bag.utils.BagProductEntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

@Service
public class BagProductServiceImpl implements BagProductService {
    @Autowired
    BagProductRepository bagProductRepository;
    @Override
    public Mono<BagProductDto> addProductToBag(Mono<BagProductDto> bagProductDto) {
        return bagProductDto
                .map(BagProductEntityDtoUtils::dtoToEntity)
                .map(bagItem -> {
                    bagItem.setLastModified(new Date());
                    return bagItem;
                })
                .flatMap(bagProductRepository::insert)
                .map(BagProductEntityDtoUtils::entityToDto);
    }

    @Override
    public Mono<Void> removeProductFromBag(String bagProductId) {
        return this.bagProductRepository
                .deleteById(bagProductId);
    }
}
