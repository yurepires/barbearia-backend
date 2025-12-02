package com.ifma.barbearia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AgendamentoInvalidoException extends RuntimeException {
    public AgendamentoInvalidoException(String message) {
        super(message);
    }
}
