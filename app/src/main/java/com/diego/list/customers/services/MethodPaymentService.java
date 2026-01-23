package com.diego.list.customers.services;

import com.diego.list.customers.application.command.CreateMethodPaymentCommand;
import com.diego.list.customers.model.MethodPayment;
import com.diego.list.customers.repository.MethodPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
            .methodPaymentId(command.getMethodPaymentId())
            .isDefault(command.isDefault())
            .brand(command.getBrand())
            .expMonth(command.getExpMonth())
            .expYear(command.getExpYear())
            .last4(command.getLast4())
            .funding(command.getFunding())
            .build();

        methodPaymentRepository.save(methodPayment);

    }
}