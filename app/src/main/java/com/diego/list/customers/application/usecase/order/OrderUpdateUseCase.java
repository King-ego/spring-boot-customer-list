package com.diego.list.customers.application.usecase.order;

import com.diego.list.customers.model.Order;
import com.diego.list.customers.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class OrderUpdateUseCase {
    private final OrderRepository orderRepository;

    public void updateOrderStatus(UUID orderId, String status, String paymentId, LocalDateTime paymentDate) {
        Optional <Order> order = orderRepository.findById(orderId);

        if (order.isEmpty()) {
            throw new IllegalArgumentException("Order not found");

        }
        orderRepository.updateParse(orderId, status, paymentId, paymentDate);
    }
}
