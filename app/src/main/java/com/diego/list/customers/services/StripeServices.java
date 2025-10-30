package com.diego.list.customers.services;

import com.diego.list.customers.errors.CustomException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.stripe.model.Customer;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class StripeServices {
    private final String stripeApiKey  = System.getenv("STRIPE_API_KEY");

    public String createCustomer(String email, String name) {
        Stripe.apiKey = stripeApiKey;

        Map<String, Object> params = new HashMap<>();

        params.put("email", email);
        params.put("name", name);

        try {
            Customer customer = Customer.create(params);
            return customer.getId();
        }catch (StripeException e){
            log.error("Error creating customer in Stripe: {}", e.getMessage());
            throw new CustomException("Error creating customer in Stripe", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}