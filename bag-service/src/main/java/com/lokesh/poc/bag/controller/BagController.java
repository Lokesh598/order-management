package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * this is for test the code and to understand the flow.
 * this not the part of production.
 */
@Controller
@RestController
@RequestMapping(value = "/api/bag/v1")
public class BagController {
    @Autowired
    private BagService bagService;

    /**
     * bag can be created for gust users
     * @param bagDto
     * @return
     */
    @PostMapping(value = "/createBag")
    public Mono<BagDto> createBag(@RequestBody Mono<BagDto> bagDto) {
        return this.bagService
                .createBag(bagDto);
    }

//    @GetMapping("/getBag/{bagId}")
//    public Mono<ResponseEntity<BagDto>> getBag(@PathVariable("bagId") String bagId) {
//        return this.bagService
//                .getBag(bagId)
//                .map(ResponseEntity::ok)
//                .log();
//    }
}
