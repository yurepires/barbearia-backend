package com.ifma.barbearia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConclusaoInvalidaException extends RuntimeException {
    public ConclusaoInvalidaException(String message) {
        super(message);
    }
}
