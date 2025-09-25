package com.diego.list.customers.controller;

import com.diego.list.customers.command.CreateMethodPaymentCommand;
import com.diego.list.customers.dto.CreateMethodPaymentDto;
import com.diego.list.customers.services.MethodPaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/method-payments")
@AllArgsConstructor
public class MethodPaymentController {

    private final MethodPaymentService methodPaymentService;

    @PostMapping
    public void createMethodPayment(@Valid @RequestBody CreateMethodPaymentDto methodPaymentDto) {
        log.info("Creating method payment");
        CreateMethodPaymentCommand command = new CreateMethodPaymentCommand(

        );
        methodPaymentService.createMethodPayment();
    }
}
