package ru.practicum.ewm.exception;

public class BadRequestException extends RuntimeException {

    private String errorMessage;

    public BadRequestException(String errorMessage, String description) {
        super(description);
        this.errorMessage = errorMessage;
    }

    public BadRequestException(String description) {
        super(description);
        errorMessage = "Error";
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
