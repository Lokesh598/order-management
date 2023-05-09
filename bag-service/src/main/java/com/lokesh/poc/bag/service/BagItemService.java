package com.lokesh.poc.bag.service;

import com.lokesh.poc.bag.dataobject.request.BagItemRequest;
import com.lokesh.poc.bag.dataobject.response.BagDO;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.dto.ItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BagItemService {
    Mono<BagItemDto> addProductToBag(Mono<BagItemDto> bagProductDto);

    Mono<Void> removeProductFromBag(String bagProductId);

    Mono<BagItemDto> addItemToBag(Mono<BagItemDto> bagItemDto);

    Mono<BagItemDto> getBagByBagId(String bagId);

    Mono<BagItemDto> retrieveBag(Mono<BagItemDto> bagItemDto);

    Mono<BagDO> getUserBag(String bagId);

    Mono<BagItemDto> updateItemsInBag(String bagId, ItemDto itemDto);
}
