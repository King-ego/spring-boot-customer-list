package com.diego.list.customers.controller;

import com.diego.list.customers.services.MethodPaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/method-payments")
@AllArgsConstructor
public class MethodPaymentController {

    private final MethodPaymentService methodPaymentService;

    @PostMapping
    public void createMethodPayment() {
        log.info("Creating method payment");
    }
}
