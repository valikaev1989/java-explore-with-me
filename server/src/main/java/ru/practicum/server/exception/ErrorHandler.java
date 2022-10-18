package ru.practicum.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.exception.models.AccessException;
import ru.practicum.server.exception.models.ConflictException;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.exception.models.ValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.practicum.server.utils.FormatDate.FORMATTER;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(NotFoundException e) {
        return ErrorResponse.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .reason("The required object was not found")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(ValidationException e) {
        return ErrorResponse.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .reason("For the requested operation the conditions are not met")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse badRequest(ConflictException e) {
        return ErrorResponse.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .status(String.valueOf(HttpStatus.CONFLICT))
                .reason("Integrity constraint has been violated")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler(AccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse badRequest(AccessException e) {
        return ErrorResponse.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .status(String.valueOf(HttpStatus.FORBIDDEN))
                .reason("For the requested operation the conditions are not met")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse badRequest(Throwable e) {
        return ErrorResponse.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .reason("Error occurred")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}