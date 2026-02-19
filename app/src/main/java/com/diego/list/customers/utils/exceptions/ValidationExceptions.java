package com.diego.list.customers.utils.exceptions;

import com.diego.list.customers.errors.CustomException;
import org.springframework.http.HttpStatus;

public class ValidationExceptions {
    private static void validateCondition(boolean condition, String message, HttpStatus status) {
        if (condition) {
            throw new CustomException(message, status);
        }
    }

    public static void validate(boolean condition, String message, HttpStatus status) {
        validateCondition(condition, message, status);
    }
}
