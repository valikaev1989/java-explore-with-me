package ru.practicum.server.exception.models;

public class AccessException extends RuntimeException {
    public AccessException(String message) {
        super(message);
    }
}