package com.diego.list.customers.controller;

import com.diego.list.customers.command.CreateOrderCommand;
import com.diego.list.customers.command.OrderItemCommand;
import com.diego.list.customers.dto.CreateOrderDto;
import com.diego.list.customers.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public void createOrder(@Valid @RequestBody CreateOrderDto orderDto) {
        log.info("Creating order {}", orderDto);

        CreateOrderCommand order = new CreateOrderCommand(
                orderDto.getCustomerId(),
                orderDto.getOrders().stream()
                        .map(item -> new OrderItemCommand(item.getProductId(), item.getAmount()))
                        .collect(Collectors.toList())
        );

        orderService.createOrder(order);

        log.info("Creating order {}", order);
    }
}
