package com.diego.list.customers.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class MethodPaymentService {
    public void createMethodPayment() {
        log.info("Creating method payment");
    }
}
