package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateOrderCommand;
import com.diego.list.customers.command.OrderItemCommand;
import com.diego.list.customers.model.Order;
import com.diego.list.customers.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void createOrder(CreateOrderCommand command) {
        List<OrderItemCommand> items = command.getOrders();
        UUID orderId = UUID.randomUUID();

        items.forEach(item -> {
            Order order = Order.builder()
                    .customerId(command.getCustomerId())
                    .productId(item.getProductId())
                    .quantity(item.getAmount())
                    .orderId(orderId)
                    .status("PENDING")
                    .build();

            orderRepository.save(order);
        });
        log.info("Creando orden");
    }
}
