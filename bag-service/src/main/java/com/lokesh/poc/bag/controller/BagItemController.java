package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dataobject.request.BagItemRequest;
import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.service.BagItemService;
import com.lokesh.poc.bag.service.BagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RestController
@RequestMapping(value = "/api/bagProduct/v1")
public class BagItemController {
    @Autowired
    private BagService bagService;
    @Autowired
    private BagItemService bagItemService;
    @PostMapping(value = "/addProduct")
    public Mono<ResponseEntity<BagItemDto>> addProductToBag(@RequestBody Mono<BagItemDto> bagProductDto) {
        return this.bagItemService
                .addProductToBag(bagProductDto)
                .map(ResponseEntity::ok)
                .log();
    }

    @PostMapping(value = "/bags")
    public Mono<ResponseEntity<BagItemDto>> addItemsToBag(@RequestParam(name = "bagId", required = true ) String bagId,
                                                          @RequestParam(name = "itemId", required = false) String itemId,
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
                        .itemId(itemId)
                        .qty(body.getQty())
                        .build()
        );
        return this.bagItemService
                .addItemToBag(bagItemDto)
                .map(ResponseEntity::ok);
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
                .log();
    }
}
