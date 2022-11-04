package ru.practicum.server.exception.models;

import javax.validation.ConstraintViolationException;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}