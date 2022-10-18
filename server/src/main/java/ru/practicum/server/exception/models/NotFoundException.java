package ru.practicum.server.exception.models;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}