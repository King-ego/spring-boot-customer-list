package com.diego.list.customers.application.validation;

import com.diego.list.customers.utils.exceptions.ValidationExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public static void validateUserExists(Boolean condition) {
        ValidationExceptions
                .validate(condition, "User is exist", HttpStatus.CONFLICT);
    }

    public static void validateCustomerDetails(Boolean condition) {
        ValidationExceptions
                .validate(condition, "Customer details are required", HttpStatus.BAD_REQUEST);
    }

    public static void validateSellerDetails(Boolean condition) {
        ValidationExceptions
                .validate(condition, "Seller details are required", HttpStatus.BAD_REQUEST);

    }

    public static void validateUserNotFound(Boolean condition) {
        ValidationExceptions
                .validate(condition, "User not found", HttpStatus.NOT_FOUND);
    }

}
