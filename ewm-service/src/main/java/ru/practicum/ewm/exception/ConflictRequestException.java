package ru.practicum.ewm.exception;

public class ConflictRequestException extends RuntimeException {

    private String errorMessage;

    public ConflictRequestException(String errorMessage, String description) {
        super(description);
        this.errorMessage = errorMessage;
    }

    public ConflictRequestException(String description) {
        super(description);
        errorMessage = "Error";
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
