package ru.practicum.ewm.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String description) {
        super(description);
    }
}
