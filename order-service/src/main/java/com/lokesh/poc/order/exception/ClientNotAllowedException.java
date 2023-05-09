package com.lokesh.poc.order.exception;

import com.lokesh.poc.order.dto.BagItemDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ClientNotAllowedException extends RuntimeException {
    public ClientNotAllowedException(String message ) {
        super( message );
    }

    public static <T> Mono<T> monoResponseClientNotAllowedException() {
        return Mono.error(new ClientNotAllowedException(
                new StringBuilder("Client not found with given uri").toString() ));
    }

    public static Flux<BagItemDto> fluxResponseClientNotAllowedException() {
        return Flux.error(new ClientNotAllowedException(
                new StringBuilder("Client not flound").toString()
        ));
    }
}
