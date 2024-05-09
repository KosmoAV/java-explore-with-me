package ru.practicum.ewm.exception;

public class DataNotFoundRequestException extends RuntimeException {

    public DataNotFoundRequestException(String description) {
        super(description);
    }
}
