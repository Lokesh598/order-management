package io.nisum.cloud.products.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

public class ProductWithIdAlreadyExistException extends RuntimeException {
    public ProductWithIdAlreadyExistException(String message ) {
        super( message );
    }
    public static <T> Mono<T> monoResponseProductWithIdAlreadyExistException(int id ) {
        return Mono.error(new ProductWithIdAlreadyExistException( new StringBuilder("Product with [")
                        .append( id )
                        .append("] already exist").toString() ));
    }
}