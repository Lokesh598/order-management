package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dto.BagDto;
import com.lokesh.poc.bag.service.BagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(controllers = BagController.class)
public class BagControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BagService bagService;

    @Test
    @DisplayName("Should return an error when invalid BagDto is provided")
    void createBagWhenInvalidBagDtoIsProvidedThenReturnError() {// Create an invalid BagDto with null values
        BagDto invalidBagDto = new BagDto(null, null, null, null);

        // Mock the BagService to return an error Mono when createBag is called
        when(bagService.createBag(any(Mono.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("Invalid BagDto")));

        // Send a POST request with the invalid BagDto
        webTestClient.post()
                .uri("/api/bag/v1/createBag")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBagDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid BagDto");

        // Verify that the BagService's createBag method was called with the invalid BagDto
        verify(bagService, times(1)).createBag(any(Mono.class));
    }

    @Test
    @DisplayName("Should create a bag successfully when valid BagDto is provided")
    void createBagWhenValidBagDtoIsProvided() {// Create a valid BagDto object
        BagDto bagDto = new BagDto(
                "bag123",
                "user123",
                LocalDate.now(),
                LocalDate.now()
        );

        // Mock the BagService's createBag method to return a Mono of the BagDto object
        when(bagService.createBag(any(Mono.class))).thenReturn(Mono.just(bagDto));

        // Send a POST request to the createBag endpoint with the valid BagDto object
        webTestClient.post()
                .uri("/api/bag/v1/createBag")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bagDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BagDto.class)
                .value(returnedBagDto -> {
                    // Assert that the returned BagDto object is equal to the original BagDto object
                    assertEquals(bagDto.getBagId(), returnedBagDto.getBagId());
                    assertEquals(bagDto.getUserId(), returnedBagDto.getUserId());
                    assertEquals(bagDto.getCreated(), returnedBagDto.getCreated());
                    assertEquals(bagDto.getLastModified(), returnedBagDto.getLastModified());
                });

        // Verify that the BagService's createBag method was called once with the valid BagDto object
        verify(bagService, times(1)).createBag(any(Mono.class));
    }

}