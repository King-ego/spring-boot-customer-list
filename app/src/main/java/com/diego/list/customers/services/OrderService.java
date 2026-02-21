package com.diego.list.customers.services;

import com.diego.list.customers.application.command.CreateOrderCommand;
import com.diego.list.customers.application.command.OrderItemCommand;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
        Optional <Customer> customerOpt = customerRepository.findByUserId(command.getUserId());
        validateCustomer(command.getUserId());

        UUID groupId = UUID.randomUUID();
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);

        command.getOrders().forEach(item ->
                processOrderItem(item, command.getUserId(), groupId, totalPrice)
        );

        log.info("Order Created: {}", totalPrice.get());
        orderRepository.flush();

        sendOrderEvent(groupId, command.getUserId(), totalPrice.get());
    }

    /*private void validateCustomer(UUID userId) {
        customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
    }*/

    private void processOrderItem(OrderItemCommand item, UUID userId, UUID groupId, AtomicReference<BigDecimal> totalPrice) {
        Product product = validateAndGetProduct(item.getProductId());

        updateProductStock(product, item.getAmount());

        createOrder(userId, groupId, item, product.getId());

        updateTotalPrice(totalPrice, product.getPrice(), item.getAmount());
    }

    private Product validateAndGetProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND));
    }

    private void updateProductStock(Product product, int requestedAmount) {
        int currentStock = product.getQuantityInStock();

        if (currentStock < requestedAmount) {
            throw new CustomException(
                    "Insufficient stock for product: " + product.getName(),
                    HttpStatus.BAD_REQUEST
            );
        }

        int updatedStock = currentStock - requestedAmount;
        productRepository.updatedParse(product.getId(), updatedStock);

    }

    private void createOrder(UUID userId, UUID groupId, OrderItemCommand item, UUID productId) {
        Order order = Order.builder()
                .customerId(userId)
                .productId(productId)
                .quantity(item.getAmount())
                .groupId(groupId)
                .status("PENDING")
                .build();

        orderRepository.save(order);
    }

    private void updateTotalPrice(AtomicReference<BigDecimal> totalPrice, BigDecimal productPrice, int quantity) {
        BigDecimal itemPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
        totalPrice.updateAndGet(current -> current.add(itemPrice));
    }

    private void sendOrderEvent(UUID groupId, UUID userId, BigDecimal totalPrice) {
        OderCreateEvent event = new OderCreateEvent(groupId, userId, "PENDING", totalPrice);
        try {
            rabbitMQProducer.sendEvent(event);
        } catch (Exception e) {
            log.error("Failed to send event, rolling back transaction", e);
            throw new CustomException("Failed to send payment event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
