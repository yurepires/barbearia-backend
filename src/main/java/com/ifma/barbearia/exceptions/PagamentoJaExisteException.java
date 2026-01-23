package com.ifma.barbearia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PagamentoJaExisteException extends RuntimeException {

    public PagamentoJaExisteException(String message) {
        super(message);
    }

}
