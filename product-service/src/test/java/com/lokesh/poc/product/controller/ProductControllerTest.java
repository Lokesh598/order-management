package com.lokesh.poc.product.controller;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @DisplayName("Should return an empty Mono when the itemId is not found")
    void getItemWhenItemIdNotFound() {
        String itemId = "12345";
        ProductDto productDto = ProductDto.builder()
                .id("1")
                .itemId(itemId)
                .name("Test Product")
                .description("Test Description")
                .category("Test Category")
                .price(10.0)
                .build();

        when(productService.getItemByItemId(itemId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/product/v1/product/{itemId}", itemId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();

        verify(productService, times(1)).getItemByItemId(itemId);
    }

    @Test
    @DisplayName("Should return the product when the itemId is valid")
    void getItemWhenItemIdIsValid() {
        String itemId = "123";
        ProductDto productDto = ProductDto.builder()
                .id("1")
                .itemId(itemId)
                .name("Test Product")
                .description("Test Description")
                .category("Test Category")
                .price(10.0)
                .build();
        when(productService.getItemByItemId(itemId)).thenReturn(Mono.just(productDto));

        webTestClient.get()
                .uri("/api/product/v1/product/{itemId}", itemId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDto.class)
                .value(returnedProductDto -> {
                    assertEquals(productDto.getId(), returnedProductDto.getId());
                    assertEquals(productDto.getItemId(), returnedProductDto.getItemId());
                    assertEquals(productDto.getName(), returnedProductDto.getName());
                    assertEquals(productDto.getDescription(), returnedProductDto.getDescription());
                    assertEquals(productDto.getCategory(), returnedProductDto.getCategory());
                    assertEquals(productDto.getPrice(), returnedProductDto.getPrice());
                });
    }

    @Test
    @DisplayName("Should add product")
    public void saveProduct() throws Exception {
        String itemId = "123";
        ProductDto productDto = new ProductDto("1", "123", "Sample Product", "this product is good", "x", 10.99);

        when(productService.getItemByItemId(itemId)).thenReturn(Mono.just(productDto));
        webTestClient.post().uri("/api/product/v1/product")
                .body(Mono.just(productDto), ProductDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDto.class);
    }

    @Test
    @DisplayName("should get item of the given id")
    void getItem() {
        String itemId = "123";
        ProductDto expectedProduct = new ProductDto("1", "123", "Sample Product", "this product is good", "x", 10.99);

        when(productService.getItemByItemId(itemId)).thenReturn(Mono.just(expectedProduct));

        webTestClient.get().uri("/api/product/v1/product/{itemId}", itemId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(ProductDto.class)
                .value(product -> {
                    assertEquals(itemId, product.getItemId());
                    assertEquals("Sample Product", product.getName());
                    assertEquals("this product is good", product.getDescription());
                    assertEquals("x", product.getCategory());
                    assertEquals(10.99, product.getPrice());
                });

        verify(productService, times(1)).getItemByItemId(itemId);
        verifyNoMoreInteractions(productService);
    }
}