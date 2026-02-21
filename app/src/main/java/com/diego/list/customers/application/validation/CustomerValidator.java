package com.diego.list.customers.application.validation;

import com.diego.list.customers.utils.exceptions.ValidationExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidator {
    public static void validateCustomerNotFound(Boolean condition) {
        ValidationExceptions.validate(
                condition, "Customer not found", HttpStatus.NOT_FOUND
        );
    }
}
