package com.jelleglebbeek.pcparts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidRecaptchaException extends RuntimeException {
    public InvalidRecaptchaException(String message) {
        super(message);
    }
}
