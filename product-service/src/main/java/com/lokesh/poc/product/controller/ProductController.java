package com.lokesh.poc.product.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.service.ProductService;
import jakarta.validation.Valid;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


/**
 * @Author: Lokesh Singh
 */

@RestController
@RequestMapping("/api/product/v1")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductKafkaController.class.getName());
    @Autowired
    private KafkaTemplate<String, Object> template;
    @Autowired
    private KafkaSender<String, Object> kafkaSender;

    KafkaConsumer<String, String> kafkaConsumer;
    @Autowired
    private ObjectMapper objectMapper;
    private String topic = "product-topic-1";

    private final String topic2 = "product-topic-2";

    private final String GROUP_ID = "jsonGroup-2";

    @Autowired
    private ProductService productService;

    /**
     * this method saving item object in db and publishing in kafka topic
     * @param productDtoMono
     * @return
     */
    @PostMapping(value = "/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ProductDto> saveItem(@RequestBody Mono<ProductDto> productDtoMono) {
//        return this.kafkaSender.createOutbound()
//                .send(productDtoMono.map(productDto -> new ProducerRecord<>(topic, "product.name", productDto)))
//                .then()
//                .log()
//                .doOnError(e -> log.error(
//                        String.format("Failed to send topic: %s value: %s", topic, productDtoMono), e))
//                .then(productDtoMono).log();
        return this.productService
                .saveProduct(productDtoMono).doOnNext( p -> template.send(topic, p));
    }

    @GetMapping(value = "/product/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductDto> getItem(@PathVariable String itemId) {
        return this.productService
                .getItemByItemId(itemId);
    }

    @PutMapping(value = "/update/{id}")
    public Mono<ProductDto> updateItemInfo(@PathVariable("id") String id, @Valid @RequestBody Mono<ProductDto> productDto) {
        return this.productService
                .updateItemInfo(id, productDto);
    }

//    @KafkaListener(topics = "product-topic-2", groupId = "jsonGroup-2", containerFactory = "productListener")
//    @GetMapping(value = "/consume/kafka-topic/update/{id}")
//    public Mono<ProductDto> updateItemInfoFromKafkaTopic(@PathVariable("id") String id) {
////        return this.productService
////                .updateItemInfo(id, productDto);
//
//    }
    @GetMapping(value = "/product/listOfItems")
    public Flux<ProductDto> getItems() throws InterruptedException {
        return this.productService.getAllItems();
    }

    /**
     * lets print hello world
     */
    @GetMapping("/welcome")
    public String printMessage() {
        return "welcome to product service";
    }
}








