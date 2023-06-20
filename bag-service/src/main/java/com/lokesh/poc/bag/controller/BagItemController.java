package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dataobject.response.BagDO;
import com.lokesh.poc.bag.dataobject.request.BagItemRequest;
import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.dto.ItemDto;
import com.lokesh.poc.bag.repository.BagRepository;
import com.lokesh.poc.bag.service.BagItemService;
import com.lokesh.poc.bag.service.BagService;
import com.lokesh.poc.bag.utils.BagEntityDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/bagItem/v1")
public class BagItemController {
    @Autowired
    private BagService bagService;

    @Autowired
    private BagRepository bagRepository;
    @Autowired
    private BagItemService bagItemService;

    /**
     * @Task create bag while adding item in bag. bagItem table and bag table should create together
     *
     * @param bagProductDto
     * @return bag items
     */
    @PostMapping(value = "/addItem")
    public Mono<BagItemDto> addProductToBag( @RequestParam Optional<String> userId,
                                                            @RequestBody Mono<BagItemDto> bagProductDto) {
        return this.bagItemService
                .addProductToBag(userId, bagProductDto);
    }

    @GetMapping(value = "/bags/{bagId}", produces = "application/json")
    public Mono<BagItemDto> getBag(@PathVariable(value = "bagId") String bagId) {
        log.info("START: BagController :: bagId: {} ", bagId);
        return bagItemService.getBagByBagId(bagId);
    }

    /**
     *  BagSummary method
     * @Result:
     * {
     *     "bagId": "645a058b44fd195e1f18ba47",
     *     "totalItem": 1,
     *     "bagItem": [
     *         {
     *             "itemId": "2f12089",
     *             "name": "Mac 16 inch Pro",
     *             "price": 499800.0,
     *             "qty": 2
     *         }
     *     ]
     * }
     *
     * @param bagId
     * @return user bag summary
     */
    @GetMapping(value = "/user-bag/{bagId}")
    public Mono<BagDO> getUserBag(@PathVariable String bagId) {
        return this.bagItemService.getUserBag(bagId);
    }


    /**
     *
     * @param bagId
     * @param body
     * @return
     */
    @PostMapping(value = "/bags", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<BagItemDto> addItemsToBag(@RequestParam(name = "bagId", required = true ) String bagId,
                                                          @RequestParam(name = "userId", required = false) String userId,
                                                          @RequestBody BagItemRequest body) {
        Mono<BagItemDto> bagItemDto = Mono.just(
                BagItemDto.builder()
                        .bagId(bagId)
                        .userId(userId)
                        .itemDto(Collections.singletonList(ItemDto.builder()
                                .itemId(body.getItemId())
                                .qty(body.getQty())
                                .build()))
                        .build()
        );
        bagItemDto.subscribe(System.out::println);
        return this.bagItemService
                .addItemToBag(bagItemDto);
    }

    /**
     * this method for add new item in users existing bag.
     *
     * @param bagId
     */
    @PutMapping(value = "/update-bag/{bagId}")
    public Mono<BagItemDto> updateItemsInBag(@PathVariable String bagId,
                                                             @RequestBody ItemDto itemDto) {
        return this.bagItemService
                .updateItemsInBag(bagId, itemDto);
    }

    /**
     * @Todo: If bag is empty handle the exception that bag is empty
     *
     */
    @DeleteMapping("/removeProduct/{bagProductId}")
    public Mono<Void> removeProductFromBag(@PathVariable String bagProductId) {
        log.info("item deleted");
        return this.bagItemService
                .removeProductFromBag(bagProductId);
    }
}
