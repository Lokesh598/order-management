package com.lokesh.poc.bag.service.impl;

import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.repository.BagRepository;
import com.lokesh.poc.bag.service.BagService;
import com.lokesh.poc.bag.utils.BagEntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;

@Service
public class BagServiceImpl implements BagService {
    @Autowired
    private BagRepository bagRepository;
    @Override
    public Mono<BagDto> createBag(Mono<BagDto> bagDto) {
        return bagDto
                .map(BagEntityDtoUtil::dtoToEntity)
                .map(bag -> {
                    bag.setCreated(LocalDate.now());
                    bag.setLastModified(LocalDate.now());
                    return bag;
                })
                .flatMap(bagRepository::insert)
                .map(BagEntityDtoUtil::entityToDto);
    }

    @Override
    public Mono<BagDto> getBag(String bagId) {
        return this.bagRepository
                .findById(bagId)
                .map(BagEntityDtoUtil::entityToDto);
    }
}
