package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateOrderCommand;
import com.diego.list.customers.command.OrderItemCommand;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.fila.exchange.RabbitMQProducer;
import com.diego.list.customers.fila.exchange.event.OderCreateEvent;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.model.Order;
import com.diego.list.customers.model.Product;
import com.diego.list.customers.repository.CustomerRepository;
import com.diego.list.customers.repository.OrderRepository;
import com.diego.list.customers.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

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
    private final ProductRepository productRepository;
    private final RabbitMQProducer rabbitMQProducer;

    public void createOrder(CreateOrderCommand command) {
        List<OrderItemCommand> items = command.getOrders();
        UUID groupId = UUID.randomUUID();

        Optional<Customer> customer = customerRepository.findById(command.getCustomerId());

        if (customer.isEmpty()) {
            throw new CustomException("Customer not found", HttpStatus.NOT_FOUND);
        }

        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);

        items.forEach(item -> {

            Optional<Product> product = productRepository.findById(item.getProductId());

            if (product.isEmpty()) {
                throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
            }

            Order order = Order.builder()
                    .customerId(command.getCustomerId())
                    .productId(item.getProductId())
                    .quantity(item.getAmount())
                    .groupId(groupId)
                    .status("PENDING")
                    .build();

            BigDecimal itemPrice = product.get().getPrice()
                    .multiply(BigDecimal.valueOf(item.getAmount()));

            totalPrice.updateAndGet(current -> current.add(itemPrice));

            orderRepository.save(order);
        });

        log.info("Order Created: {}", totalPrice.get());

        OderCreateEvent event = new OderCreateEvent(
                groupId,
                command.getCustomerId(),
                "PENDING",
                totalPrice.get()
        );

        rabbitMQProducer.sendEvent(event);

    }
}
