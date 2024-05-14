package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictRequestException;
import ru.practicum.ewm.exception.DataNotFoundRequestException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final MissingRequestHeaderException exception) {

        log.info("Get MissingRequestHeaderException from {}", exception.getParameter());

        return new ApiError(List.of("MissingRequestHeaderException"), exception.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final IllegalArgumentException exception) {

        log.info("Get IllegalArgumentException: {}", exception.getMessage());

        return new ApiError(List.of("IllegalArgumentException"), exception.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final MethodArgumentNotValidException exception) {

        log.info("Get ValidationException: {}", exception.getParameter());

        return new ApiError(List.of("MethodArgumentNotValidException"), exception.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final ConstraintViolationException exception) {

        log.info("Get ValidationException: {}", exception.getMessage());

        return new ApiError(List.of("ConstraintViolationException"), exception.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final BadRequestException exception) {

        log.info("Get DataBadRequestException: {}", exception.getMessage());

        return new ApiError(List.of("BadRequestException"), exception.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(final ConflictRequestException exception) {

        log.info("Get ConflictRequestException: {}", exception.getMessage());

        return new ApiError(List.of("ConflictRequestException"), exception.getMessage(),
                HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(final DataNotFoundRequestException exception) {

        log.info("Get DataNotFoundRequestException: {}", exception.getMessage());

        return new ApiError(List.of("DataNotFoundRequestException"), exception.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(final SQLException exception) {

        log.info("Get SQLException: {}", exception.getMessage());

        return new ApiError(List.of("SQLException"), exception.getMessage(),
                HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.toString());
    }
}