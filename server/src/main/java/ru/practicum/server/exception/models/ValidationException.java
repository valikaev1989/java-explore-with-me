package ru.practicum.server.exception.models;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}