package ru.practicum.server.exception.models;

public class IllegalStateException extends RuntimeException {
    public IllegalStateException(String message) {
        super(message);
    }
}