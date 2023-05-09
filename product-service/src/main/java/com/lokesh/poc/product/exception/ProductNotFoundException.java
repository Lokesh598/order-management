package com.lokesh.poc.product.exception;

import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message ) {
        super( message );
    }

    public static <T> Mono<T> monoResponseProductNotFoundException(String id, String name ) {
        return Mono.error(new ProductNotFoundException(
                new StringBuilder("😬😬 Product with [")
                        .append(StringUtils.isEmpty(id)     ? "" : id   )
                        .append(StringUtils.isEmpty(name)   ? "" : name )
                        .append("] not found, we will add soon ☺☺").toString() ));
    }

}
