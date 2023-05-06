package com.lokesh.poc.user.exception;

import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException (String message) {
        super(message);
    }
    public static <T> Mono<T> monoResponseProductNotFoundException(String emailId, String name ) {
        return Mono.error(new UserNotFoundException(
                new StringBuilder("User with [")
                        .append(StringUtils.isEmpty(emailId)     ? "" : emailId   )
                        .append(StringUtils.isEmpty(name)   ? "" : name )
                        .append("] not found").toString() ));
    }
}
