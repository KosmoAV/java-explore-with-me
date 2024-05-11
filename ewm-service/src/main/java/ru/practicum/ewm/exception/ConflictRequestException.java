package ru.practicum.ewm.exception;

public class ConflictRequestException extends RuntimeException {

    public ConflictRequestException(String description) {
        super(description);
    }
}
