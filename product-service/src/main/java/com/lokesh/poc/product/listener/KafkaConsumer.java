package com.lokesh.poc.product.listener;

import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Component
@Service
public class KafkaConsumer {
    @Autowired
    private ProductService productService;

    ProductDto message = new ProductDto();
    @KafkaListener(topics = "product-topic-2", groupId = "productDto-group", containerFactory = "productListener")
    public void consume(ProductDto productDto)
    {
        String id = productDto.getId();
        Mono<ProductDto> productDtoMono = Mono.just(productDto);
        System.out.println("message = " + productDto);

//        this.productService.updateItemInfo(id, productDtoMono).block();
        this.productService.updateOrInsertItem(id, productDtoMono).block();
    }
}
