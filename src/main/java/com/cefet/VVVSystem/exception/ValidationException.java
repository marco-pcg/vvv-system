package com.cefet.VVVSystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends BaseException {

    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message, HttpStatus.BAD_REQUEST);
        this.errors = errors;
    }

    public ValidationException(String message, String error) {
        this(message, List.of(error));
    }
}
