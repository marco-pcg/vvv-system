package com.cefet.VVVSystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {

    private final HttpStatus status;

    protected BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    protected BaseException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
