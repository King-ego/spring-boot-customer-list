package com.diego.list.customers.application.validation;

import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.User;
import com.diego.list.customers.model.UserRole;
import com.diego.list.customers.utils.exceptions.ValidationExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {

    public static void validateUserExists(Boolean condition) {
        ValidationExceptions
                .validate(condition, "User is exist", HttpStatus.CONFLICT);
    }

    public void validateEmailNotInUse(Optional<User> existingUser) {
        if (existingUser.isPresent()) {
            throw new CustomException("Email already in use", HttpStatus.CONFLICT);
        }
    }

    public void validateCustomerDetails(UserRole role, Object customerDetails) {
        if (role == UserRole.CUSTOMER && customerDetails == null) {
            throw new CustomException("Customer details are required", HttpStatus.BAD_REQUEST);
        }
    }

    public void validateSellerDetails(UserRole role, Object sellerDetails) {
        if (role == UserRole.SELLER && sellerDetails == null) {
            throw new CustomException("Seller details are required", HttpStatus.BAD_REQUEST);
        }
    }

    public void validateUserNotNull(User user) {
        if (user == null) {
            throw new CustomException("User cannot be null", HttpStatus.BAD_REQUEST);
        }
    }
}

