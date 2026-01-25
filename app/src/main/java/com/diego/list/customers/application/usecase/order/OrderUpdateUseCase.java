package com.diego.list.customers.application.usecase.order;

import com.diego.list.customers.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class OrderUpdateUseCase {
    private final OrderRepository orderRepository;

    public void updateOrderStatus(UUID orderId, String status, String paymentId) {
        // Method implementation goes here
    }
}
