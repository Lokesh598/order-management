package com.lokesh.poc.bag.service;

import com.lokesh.poc.bag.dto.BagDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface BagService {
    Mono<BagDto> createBag(Mono<BagDto> bagDto);

    Mono<BagDto> getBag(String bagId);
}
