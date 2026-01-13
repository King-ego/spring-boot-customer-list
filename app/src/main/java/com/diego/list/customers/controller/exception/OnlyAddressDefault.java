package com.diego.list.customers.controller.exception;

import com.diego.list.customers.errors.CustomException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@NoArgsConstructor
public class OnlyAddressDefault {
    public void validate(boolean isDefault, UUID userId) {
        if (!isDefault) {
            return;
        }
    }
}
