package com.fenris06.applicationmanager.controller;


import com.fenris06.applicationmanager.exception.ArgumentException;
import com.fenris06.applicationmanager.exception.DataValidationException;
import com.fenris06.applicationmanager.exception.ErrorResponse;
import com.fenris06.applicationmanager.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Throwable e) {
        log.error("throwableHandler", e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "something unusual.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(final NotFoundException e) {
        log.error("notFoundHandler", e);
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFormatHandler(final NumberFormatException e) {
        log.error("notFormatHandle", e);
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        FieldError error = e.getFieldError();
        if (error == null) {
            log.error("methodArgumentNotValidException", e);
            return new ErrorResponse(
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Incorrectly made request.",
                    e.getMessage(),
                    LocalDateTime.now()
            );
        } else {
            log.error("methodArgumentNotValidException", e);
            return new ErrorResponse(
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Incorrectly made request.",
                    error.getDefaultMessage(),
                    LocalDateTime.now()
            );
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("constraintViolationExceptionHandler", e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dataIntegrityViolationExceptionHandler(final DataIntegrityViolationException e) {
        log.error("dataIntegrityViolationExceptionHandler", e);
        return new ErrorResponse(
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dataValidationExceptionHandler(final DataValidationException e) {
        log.error("dataValidationExceptionHandler", e);
        return new ErrorResponse(
                "FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse argumentHandler(final ArgumentException e) {
        log.error("argumentHandler", e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

}
