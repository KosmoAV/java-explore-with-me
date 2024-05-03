package ru.practicum.ewm.exception;

public class DataNotFoundRequestException extends RuntimeException {

    private String errorMessage;

    public DataNotFoundRequestException(String errorMessage, String description) {
        super(description);
        this.errorMessage = errorMessage;
    }

    public DataNotFoundRequestException(String description) {
        super(description);
        errorMessage = "Error";
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
