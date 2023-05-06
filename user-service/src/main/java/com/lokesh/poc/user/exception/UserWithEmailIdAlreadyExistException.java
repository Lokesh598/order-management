package com.lokesh.poc.user.exception;

import reactor.core.publisher.Mono;

public class UserWithEmailIdAlreadyExistException extends RuntimeException{
    public UserWithEmailIdAlreadyExistException(String message ) {
        super( message );
    }
    public static <T> Mono<T> monoResponseUserWithEmailIdAlreadyExistException(String emailId ) {
        return Mono.error(new UserWithEmailIdAlreadyExistException( new StringBuilder("Product with [")
                .append( emailId )
                .append("] already exist").toString() ));
    }
}
