package com.diego.list.customers.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Erro de validação");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleError(HttpStatus.BAD_REQUEST.value(), message));
    }

    static class SimpleError(int status, String message) {
        public int status;
        public String message;
    }
}
