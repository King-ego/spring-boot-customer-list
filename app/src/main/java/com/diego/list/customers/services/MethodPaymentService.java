package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateMethodPaymentCommand;
import com.diego.list.customers.model.MethodPayment;
import com.diego.list.customers.repository.MethodPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class MethodPaymentService {
    private final MethodPaymentRepository methodPaymentRepository;
    public void createMethodPayment(CreateMethodPaymentCommand command) {
        MethodPayment methodPayment = MethodPayment.builder()
            .customerId(command.getCustomerId())
            .type(command.getType())
            .method_payment_id(command.getMethod_payment_id())
            .isDefault(command.isDefault())
            .brand(command.getBrand())
            .exp_month(command.getExp_month())
            .exp_year(command.getExp_year())
            .last4(command.getLast4())
            .funding(command.getFunding())
            .build();


        log.info("Creating method payment: {}", methodPayment);
    }
}
