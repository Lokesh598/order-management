package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dataobject.request.BagItemRequest;
import com.lokesh.poc.bag.dataobject.response.BagDO;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.dto.ItemDto;
import com.lokesh.poc.bag.repository.BagRepository;
import com.lokesh.poc.bag.service.BagItemService;
import com.lokesh.poc.bag.service.BagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BagItemController")
class BagItemControllerTest {
    @Mock
    private BagRepository bagRepository;
    @Mock
    private BagService bagService;
    @Mock
    private BagItemService bagItemService;
    @InjectMocks
    private BagItemController bagItemController;

//        @Test
//    @DisplayName("Should return an error when bagId is not provided")
//    void addItemsToBagWhenBagIdNotProvidedThenReturnError() {
//        String bagId = null;
//        String userId = "user1";
//        BagItemRequest body = new BagItemRequest("item1", 2);
//            Mono<BagItemDto> bagItemDto = Mono.just(
//                    BagItemDto.builder()
//                            .bagId(null)
//                            .userId(userId)
//                            .itemDto(Collections.singletonList(ItemDto.builder()
//                                    .itemId(body.getItemId())
//                                    .qty(body.getQty())
//                                    .build()))
//                            .build()
//            );
//        Mono<BagItemDto> result = bagItemController.addItemsToBag(null, userId, body);
//
//        StepVerifier.create(bagItemDto)
//                .expectError(IllegalArgumentException.class)
//                .verify();
//        verifyNoInteractions(bagItemService);
//    }


    @Test
    @DisplayName("Should remove the product from the bag when the bagProductId is valid")
    void removeProductFromBagWhenBagProductIdIsValid() {
        String bagProductId = "bagProductId";
        when(bagItemService.removeProductFromBag(bagProductId)).thenReturn(Mono.empty());

        Mono<Void> result = bagItemController.removeProductFromBag(bagProductId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(bagItemService, times(1)).removeProductFromBag(bagProductId);
    }

    @Test
    @DisplayName("Should return an error when the bagProductId is not found")
    void removeProductFromBagWhenBagProductIdNotFound() {
        String bagProductId = "123";
        when(bagItemService.removeProductFromBag(bagProductId)).thenReturn(Mono.empty());

        Mono<Void> result = bagItemController.removeProductFromBag(bagProductId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(bagItemService, times(1)).removeProductFromBag(bagProductId);
    }

    @Test
    @DisplayName("Should return an error when the bagId is not found")
    void updateItemsInBagWhenBagIdNotFound() {
        String bagId = "123";
        ItemDto itemDto = ItemDto.builder().itemId("item1").qty(2).build();
        when(bagItemService.updateItemsInBag(anyString(), any(ItemDto.class)))
                .thenReturn(Mono.empty());

        Mono<BagItemDto> result = bagItemController.updateItemsInBag(bagId, itemDto);

        StepVerifier.create(result)
                .expectNextCount(0)
                .expectComplete()
                .verify();
        verify(bagItemService, times(1)).updateItemsInBag(bagId, itemDto);
    }

    @Test
    @DisplayName("Should update the items in the bag successfully")
    void updateItemsInBagSuccessfully() {// create test data
        String bagId = "bag-123";
        ItemDto itemDto = ItemDto.builder()
                .itemId("item-123")
                .qty(2)
                .build();
        BagItemDto bagItemDto = BagItemDto.builder()
                .id("bag-item-123")
                .bagId(bagId)
                .userId("user-123")
                .itemDto(Collections.singletonList(itemDto))
                .lastModified(LocalDate.now())
                .build();

        // mock the service method
        when(bagItemService.updateItemsInBag(anyString(), any(ItemDto.class)))
                .thenReturn(Mono.just(bagItemDto));

        // call the controller method
        Mono<BagItemDto> result = bagItemController.updateItemsInBag(bagId, itemDto);

        // verify the result
        StepVerifier.create(result)
                .assertNext(updatedBagItemDto -> {
                    assertNotNull(updatedBagItemDto);
                    assertEquals(bagId, updatedBagItemDto.getBagId());
                    assertEquals(1, updatedBagItemDto.getItemDto().size());
                    assertEquals(itemDto.getItemId(), updatedBagItemDto.getItemDto().get(0).getItemId());
                    assertEquals(itemDto.getQty(), updatedBagItemDto.getItemDto().get(0).getQty());
                })
                .verifyComplete();

        // verify the service method was called with correct arguments
        verify(bagItemService, times(1)).updateItemsInBag(eq(bagId), eq(itemDto));
    }

    @Test
    @DisplayName("Should return an error when the itemDto is invalid")
    void updateItemsInBagWhenItemDtoIsInvalid() {// create a mock ItemDto object with invalid data
        ItemDto itemDto = ItemDto.builder()
                .itemId(null)
                .qty(-1)
                .build();

        // create a mock BagItemDto object with valid data
        BagItemDto bagItemDto = BagItemDto.builder()
                .id("1")
                .bagId("bag-1")
                .userId("user-1")
                .itemDto(Collections.singletonList(itemDto))
                .lastModified(LocalDate.now())
                .build();

        // mock the bagItemService to return the mock BagItemDto object
        when(bagItemService.updateItemsInBag(anyString(), any(ItemDto.class)))
                .thenReturn(Mono.just(bagItemDto));

        // call the updateItemsInBag method of the BagItemController with the mock data
        Mono<BagItemDto> result = bagItemController.updateItemsInBag("bag-1", itemDto);

        // verify that the result is not null
        assertNotNull(result);

        // verify that the result contains the expected data
        StepVerifier.create(result)
                .assertNext(bagItem -> {
                    assertEquals(bagItem.getId(), "1");
                    assertEquals(bagItem.getBagId(), "bag-1");
                    assertEquals(bagItem.getUserId(), "user-1");
                    assertEquals(bagItem.getItemDto().size(), 1);
                    assertEquals(bagItem.getItemDto().get(0).getItemId(), null);
                    assertEquals(bagItem.getItemDto().get(0).getQty(), -1);
                    assertNotNull(bagItem.getLastModified());
                })
                .verifyComplete();

        // verify that the bagItemService was called with the expected parameters
        verify(bagItemService, times(1)).updateItemsInBag("bag-1", itemDto);
    }

    @Test
    @DisplayName("Should add items to the bag when bagId and item details are provided")
    void addItemsToBagWhenBagIdAndItemDetailsProvided() {// create a mock BagItemDto object
        BagItemDto bagItemDto = BagItemDto.builder()
                .id("1")
                .bagId("bag-1")
                .userId("user-1")
                .itemDto(Collections.singletonList(ItemDto.builder()
                        .itemId("item-1")
                        .qty(2)
                        .build()))
                .lastModified(LocalDate.now())
                .build();

        // create a mock BagItemRequest object
        BagItemRequest bagItemRequest = new BagItemRequest("item-1", 2);

        // create a mock Mono<BagItemDto> object
        Mono<BagItemDto> bagItemDtoMono = Mono.just(bagItemDto);

        // mock the addItemToBag method of BagItemService
        when(bagItemService.addItemToBag(any(Mono.class))).thenReturn(bagItemDtoMono);

        // call the addItemsToBag method of BagItemController
        Mono<BagItemDto> result = bagItemController.addItemsToBag("bag-1", "user-1", bagItemRequest);

        // verify the result
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals("1", response.getId());
                    assertEquals("bag-1", response.getBagId());
                    assertEquals("user-1", response.getUserId());
                    assertEquals(1, response.getItemDto().size());
                    assertEquals("item-1", response.getItemDto().get(0).getItemId());
                    assertEquals(2, response.getItemDto().get(0).getQty());
                    assertNotNull(response.getLastModified());
                })
                .verifyComplete();

        // verify that the addItemToBag method of BagItemService is called once
        verify(bagItemService, times(1)).addItemToBag(any(Mono.class));
    }

    @Test
    @DisplayName("Should return an empty Mono when the bagId is not found")
    void getUserBagWhenBagIdIsNotFound() {
        String bagId = "123";
        when(bagItemService.getUserBag(bagId)).thenReturn(Mono.empty());

        Mono<BagDO> result = bagItemController.getUserBag(bagId);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
        verify(bagItemService, times(1)).getUserBag(bagId);
    }

    @Test
    @DisplayName("Should return the user bag when the bagId is valid")
    void getUserBagWhenBagIdIsValid() {// create a mock BagDO object
        BagDO bagDO = new BagDO("bagId", 2, Collections.emptyList());

        // mock the bagItemService.getUserBag method to return the mock BagDO object
        when(bagItemService.getUserBag(anyString())).thenReturn(Mono.just(bagDO));

        // call the getUserBag method of the BagItemController with a valid bagId
        Mono<BagDO> result = bagItemController.getUserBag("bagId");

        // verify that the result is not null
        assertNotNull(result);

        // verify that the result is of type Mono<BagDO>
        StepVerifier.create(result)
                .expectNextMatches(bag -> bag.getBagId().equals("bagId"))
                .verifyComplete();

        // verify that the bagItemService.getUserBag method is called exactly once with the given bagId
        verify(bagItemService, times(1)).getUserBag("bagId");
    }

    @Test
    @DisplayName("Should return an empty response when the bagId is not found")
    void getBagWhenBagIdIsNotFound() {
        String bagId = "123";
        when(bagItemService.getBagByBagId(bagId)).thenReturn(Mono.empty());

        Mono<BagItemDto> result = bagItemController.getBag(bagId);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
        verify(bagItemService, times(1)).getBagByBagId(bagId);
    }

    @Test
    @DisplayName("Should return the bag item when the bagId is valid")
    void getBagWhenBagIdIsValid() {
        String bagId = "bag-123";
        BagItemDto bagItemDto = BagItemDto.builder()
                .id("item-123")
                .bagId(bagId)
                .userId("user-123")
                .itemDto(Collections.singletonList(ItemDto.builder()
                        .itemId("item-1")
                        .qty(2)
                        .build()))
                .lastModified(LocalDate.now())
                .build();
        when(bagItemService.getBagByBagId(bagId)).thenReturn(Mono.just(bagItemDto));

        Mono<BagItemDto> result = bagItemController.getBag(bagId);

        assertNotNull(result);
        assertEquals(bagItemDto, result.block());
        verify(bagItemService, times(1)).getBagByBagId(bagId);
    }

    @Test
    @DisplayName("Should return an error when the bagProductDto is invalid")
    void addProductToBagWhenBagProductDtoIsInvalid() {
        BagItemDto bagItemDto = BagItemDto.builder()
                .bagId("bagId")
                .userId("userId")
                .itemDto(Collections.singletonList(ItemDto.builder()
                        .itemId("itemId")
                        .qty(1)
                        .build()))
                .build();
        when(bagItemService.addProductToBag(any(), any())).thenReturn(Mono.just(bagItemDto));

        Mono<BagItemDto> result = bagItemController.addProductToBag(Optional.empty(), Mono.just(bagItemDto));

        assertNotNull(result);
        assertEquals(bagItemDto, result.block());
        verify(bagItemService, times(1)).addProductToBag(any(), any());
    }

    @Test
    @DisplayName("Should add a product to the bag when the user ID is not provided")
    void addProductToBagWhenUserIdIsNotProvided() {// create a mock BagItemDto object
        BagItemDto bagItemDto = BagItemDto.builder()
                .id("1")
                .bagId("123")
                .userId("user123")
                .itemDto(Collections.singletonList(ItemDto.builder()
                        .itemId("item123")
                        .qty(2)
                        .build()))
                .build();

        // create a mock Mono object
        Mono<BagItemDto> bagItemDtoMono = Mono.just(bagItemDto);

        // create a mock Optional object
        Optional<String> userId = Optional.empty();

        // mock the addProductToBag method of BagItemService
        when(bagItemService.addProductToBag(userId, bagItemDtoMono)).thenReturn(bagItemDtoMono);

        // call the addProductToBag method of BagItemController
        Mono<BagItemDto> result = bagItemController.addProductToBag(userId, bagItemDtoMono);

        // verify that the addProductToBag method of BagItemService is called once
        verify(bagItemService, times(1)).addProductToBag(userId, bagItemDtoMono);

        // assert that the result is not null
        assertNotNull(result);

        // assert that the result is equal to the mock BagItemDto object
        assertEquals(bagItemDto, result.block());
    }

    @Test
    @DisplayName("Should add a product to the bag when the user ID is provided")
    void addProductToBagWhenUserIdIsProvided() {// create a mock BagItemDto object
        BagItemDto bagItemDto = BagItemDto.builder()
                .id("1")
                .bagId("bag1")
                .userId("user1")
                .itemDto(Collections.singletonList(ItemDto.builder()
                        .itemId("item1")
                        .qty(1)
                        .build()))
                .build();

        // create a mock Mono<BagItemDto> object
        Mono<BagItemDto> bagItemDtoMono = Mono.just(bagItemDto);

        // create a mock Optional<String> object
        Optional<String> userId = Optional.of("user1");

        // mock the addProductToBag method of BagItemService
        when(bagItemService.addProductToBag(userId, bagItemDtoMono)).thenReturn(bagItemDtoMono);

        // call the addProductToBag method of BagItemController
        Mono<BagItemDto> result = bagItemController.addProductToBag(userId, bagItemDtoMono);

        // verify that the addProductToBag method of BagItemService is called once
        verify(bagItemService, times(1)).addProductToBag(userId, bagItemDtoMono);

        // assert that the result is not null
        assertNotNull(result);
    }
}