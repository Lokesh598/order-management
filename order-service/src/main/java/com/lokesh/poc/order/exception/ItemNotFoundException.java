package com.lokesh.poc.order.exception;

import com.lokesh.poc.order.dto.ItemDto;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(String message ) {
        super( message );
    }

    public static <T> Mono<T> monoResponseItemNotFoundException(String id, String name ) {
        return Mono.error(new ItemNotFoundException (
                new StringBuilder("Product with [")
                        .append(StringUtils.isEmpty(id)     ? "" : id   )
                        .append(StringUtils.isEmpty(name)   ? "" : name )
                        .append("] not found").toString() ));
    }

}
