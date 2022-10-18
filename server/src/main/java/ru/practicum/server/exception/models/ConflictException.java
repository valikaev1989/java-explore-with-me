package ru.practicum.server.exception.models;

import org.springframework.dao.DataIntegrityViolationException;

public class ConflictException extends DataIntegrityViolationException {
    public ConflictException(String message) {
        super(message);
    }
}