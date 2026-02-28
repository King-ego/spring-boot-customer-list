package com.diego.list.customers.application.validation;

import com.diego.list.customers.utils.exceptions.ValidationExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public static void exceptionUserExists(Boolean condition) {
        ValidationExceptions
                .validate(condition, "User is exist", HttpStatus.CONFLICT);
    }

    public static void exceptionCustomerDetails(Boolean condition) {
        ValidationExceptions
                .validate(condition, "Customer details are required", HttpStatus.BAD_REQUEST);
    }

    public static void exceptionSellerDetails(Boolean condition) {
        ValidationExceptions
                .validate(condition, "Seller details are required", HttpStatus.BAD_REQUEST);

    }

    public static void exceptionUserNotFound(Boolean condition) {
        ValidationExceptions
                .validate(condition, "User not found", HttpStatus.NOT_FOUND);
    }

    public static void exceptionDisabledUser(Boolean condition) {
        ValidationExceptions
                .validate(condition, "User is disabled", HttpStatus.FORBIDDEN);
    }

}
