package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateOrderCommand;
import com.diego.list.customers.command.OrderItemCommand;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.model.Order;
import com.diego.list.customers.repository.CustomerRepository;
import com.diego.list.customers.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public void createOrder(CreateOrderCommand command) {
        List<OrderItemCommand> items = command.getOrders();
        UUID orderId = UUID.randomUUID();

        Optional<Customer> customer = customerRepository.findById(command.getCustomerId());

        if (customer.isEmpty()) {
            throw new CustomException("Customer not found", HttpStatus.NOT_FOUND);
        }

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

    }
}
