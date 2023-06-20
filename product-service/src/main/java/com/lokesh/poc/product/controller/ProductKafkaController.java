package com.lokesh.poc.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.service.ProductService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;

@RestController
public class ProductKafkaController {
    private static final Logger log = LoggerFactory.getLogger(ProductKafkaController.class.getName());
    @Autowired
    private KafkaTemplate<String, Object> template;
    @Autowired
    ProductService service;
    @Autowired
    private KafkaSender<String, Object> kafkaSender;
    @Autowired
    private ObjectMapper objectMapper;
    private String topic = "kafka.integrating.product-topic";

    @Autowired
    private ProductService productService;

    @GetMapping("/publish/product-list")
    public Flux<ProductDto> sendMessages() throws InterruptedException, JsonProcessingException {
        Flux<ProductDto> productDtoFlux = this.productService.getAllItems();
        return this.kafkaSender.createOutbound()
                .send(productDtoFlux.map(productDto -> new ProducerRecord<>(topic, "product.name", productDto)))
                .then()
                .doOnError(e -> log.error(
                        String.format("Failed to send topic: %s value: %s", topic, productDtoFlux), e))
                .thenMany(productDtoFlux);
    }
}