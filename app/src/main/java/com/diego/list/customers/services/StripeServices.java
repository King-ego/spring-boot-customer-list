package com.diego.list.customers.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class StripeServices {
    private final String stripeApiKey  = System.getenv("STRIPE_API_KEY");

    public void createCustomer() {
        log.info("Creating Stripe Customer {}", stripeApiKey);
    }
}
