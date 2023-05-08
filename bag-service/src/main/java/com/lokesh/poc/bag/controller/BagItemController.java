package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dataobject.request.BagItemRequest;
import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.service.BagItemService;
import com.lokesh.poc.bag.service.BagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RestController
@RequestMapping(value = "/api/bagItem/v1")
public class BagItemController {
    @Autowired
    private BagService bagService;
    @Autowired
    private BagItemService bagItemService;
    @PostMapping(value = "/addItem")
    public Mono<ResponseEntity<BagItemDto>> addProductToBag(@RequestBody Mono<BagItemDto> bagProductDto) {
        return this.bagItemService
                .addProductToBag(bagProductDto)
                .map(ResponseEntity::ok)
                .log();
    }

    @GetMapping(value = "/bags/{bagId}", produces = "application/json")
    public Flux<BagItemDto> getBag(@PathVariable(value = "bagId") String bagId) {
        log.info("START: BagController :: bagId: {} ", bagId);
//        Mono<BagItemDto> bagItemDto = Mono.just(
//                BagItemDto.builder()
//                        .bagId(bagId)
//                        .itemId(itemId)
//                        .build()
//        );
//        return this.bagItemService
//                .retrieveBag(bagItemDto)
//                .map(ResponseEntity::ok);
        return bagItemService.getBagByBagId(bagId);
//        return bagItemService.getBagByBagId(bagId)
//                .map(dto-> {
//                    if (null != null) {
//                        log.info("BagItemController :: getBag(..) method is calling with bagId " , bagId);
//                    }
//                    return dto;
//                })
//                .map(bagDto -> ResponseEntity.ok().body(bagDto))
//                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping(value = "/bags", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<BagItemDto>> addItemsToBag(@RequestParam(name = "bagId", required = true ) String bagId,
                                                          @RequestBody BagItemRequest body) {

//        BagItemDto bagItemDto = null;
//        BagDto bagDto = null;
//        BagRequestDto bagRequestDto = null;
//        if (StringUtils.isBlank(bagId)) {
//            bagDto = bagService.addItemsToEmptyBag();
//        } else {
//            bagDto = bagService.addItemsToCurrentBag();
//        }
//        return
//        int quantity = body.getItemDto().getQty();
        Mono<BagItemDto> bagItemDto = Mono.just(
                BagItemDto.builder()
                        .bagId(bagId)
                        .itemId(body.getItemId())
                        .qty(body.getQty())
                        .build()
        );
        return this.bagItemService
                .addItemToBag(bagItemDto)
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
        return this.bagItemService
                .removeProductFromBag(bagProductId)
                .map(ResponseEntity::ok)
                .log("item deleted");
    }
}
