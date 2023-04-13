package com.alsomeb.shopletapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ShoppingListNotFoundException extends RuntimeException {

    public ShoppingListNotFoundException(String message) {
        super(message);
    }
}
