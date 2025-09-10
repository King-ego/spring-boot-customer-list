package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateOrderCommand;
import com.diego.list.customers.command.OrderItemCommand;
import com.diego.list.customers.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void CreateOrder(CreateOrderCommand order) {
        List<OrderItemCommand> items = order.getOrders();


        log.info("Creando orden");
    }
}
