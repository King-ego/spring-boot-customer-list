package com.diego.list.customers.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public String createCustomer(String email, String name) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Map<String, Object> params = new HashMap<>();

        params.put("email", email);
        params.put("name", name);

        Customer customer = Customer.create(params);

        log.info("Customer created in Stripe");

        return customer.getId();
    }
}
