package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dto.BagProductDto;
import com.lokesh.poc.bag.service.BagProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RestController
@RequestMapping(value = "/api/bagProduct/v1")
public class BagProductController {
    @Autowired
    private BagProductService bagProductService;

    @PostMapping(value = "/addProduct")
    public Mono<ResponseEntity<BagProductDto>> addProductToBag(@RequestBody Mono<BagProductDto> bagProductDto) {
        return this.bagProductService
                .addProductToBag(bagProductDto)
                .map(ResponseEntity::ok)
                .log();
    }

    /**
     * @Todo: If bag is empty handle the exception that bag is empty
     *
     */
    @DeleteMapping("/removeProduct/{bagProductId}")
    public Mono<ResponseEntity<Void>> removeProductFromBag(@PathVariable String bagProductId) {
        log.info("item deleted");
        return this.bagProductService
                .removeProductFromBag(bagProductId)
                .map(ResponseEntity::ok)
                .log();
    }
}
