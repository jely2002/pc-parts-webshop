package com.jelleglebbeek.pcparts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMediaFormatException extends RuntimeException {
    public UnsupportedMediaFormatException(String message) {
        super(message);
    }
}
