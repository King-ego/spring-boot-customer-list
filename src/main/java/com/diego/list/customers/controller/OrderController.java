package com.diego.list.customers.controller;

import com.diego.list.customers.command.CreateOrderCommand;
import com.diego.list.customers.command.OrderItemCommand;
import com.diego.list.customers.dto.CreateOrderDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/orders")
public class OrderController {
    @PostMapping
    public void createOrder(@Valid @RequestBody CreateOrderDto orderDto) {
        log.info("Creating order {}", orderDto);

        CreateOrderCommand order = new CreateOrderCommand(
                orderDto.getCustomerId(),
                orderDto.getOrders().stream()
                        .map(item -> new OrderItemCommand(item.getProductId(), item.getAmount()))
                        .collect(Collectors.toList())
        );

        log.info("Creating order {}", order);
    }
}
