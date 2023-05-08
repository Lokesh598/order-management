package com.lokesh.poc.order.exception;

import reactor.core.publisher.Mono;

public class ClientNotAllowedException extends RuntimeException {
    public ClientNotAllowedException(String message ) {
        super( message );
    }

    public static <T> Mono<T> monoResponseClientNotAllowedException() {
        return Mono.error(new ClientNotAllowedException(
                new StringBuilder("Products not found with given filter").toString() ));
    }
}
