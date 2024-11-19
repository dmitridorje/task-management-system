package com.test.taskmanagementsystem.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            errorResponse.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.error("Validation failed. Method Argument Not Valid Exception", ex);
        return errorResponse;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity Not Found", ex);
        return new ErrorResponse("Entity Not Found", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Argument Type Mismatch", ex);
        return new ErrorResponse("Argument Type Mismatch", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal Argument", ex);
        return new ErrorResponse("Illegal Argument", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Invalid Input Format: {}", ex.getMessage());
        return new ErrorResponse("Invalid Input Format", ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        log.error("Method Not Allowed", ex);
        return new ErrorResponse("Method Not Allowed", ex.getMessage());
    }


    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleResponseStatusException(ResponseStatusException ex) {
        log.error("Bad Request", ex);
        return new ErrorResponse("Bad Request", ex.getReason());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().iterator().next().getMessage();
        log.error("Validation Error", ex);
        return new ErrorResponse("Validation Error", message);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherRuntimeExceptions(RuntimeException ex) {
        log.error("Runtime Exception", ex);
        return new ErrorResponse("Runtime Exception", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllOtherExceptions(Exception ex) {
        log.error("Exception", ex);
        return new ErrorResponse("Exception", ex.getMessage());
    }
}
