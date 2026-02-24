package com.diego.list.customers.application.validation;

import com.diego.list.customers.utils.exceptions.ValidationExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AddressValidator {
    public static void  exceptionDefaultAddress(Boolean condition) {
        ValidationExceptions
                .validate(condition, "Default address cannot be deleted", HttpStatus.BAD_REQUEST);
    }

        public static void exceptionAddressNotFound(Boolean condition) {
            ValidationExceptions
                    .validate(condition, "Address not found", HttpStatus.NOT_FOUND);
        }
}
