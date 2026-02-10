package com.diego.list.customers.application.usecase.order;

import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Order;
import com.diego.list.customers.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class OrderUpdateUseCase {
    private final OrderRepository orderRepository;

    public void updateOrderStatus(UUID groupId, String status, String paymentId, LocalDateTime paymentDate) {
        List <Order> order = orderRepository.findAllGroupId(groupId);

        if (order.isEmpty()) {
            log.warn("No orders found for groupId: {}", groupId);
            throw new CustomException("Orders not found for the provided groupId", HttpStatus.NOT_FOUND);
        }

        order.forEach(o -> orderRepository.updateParse(o.getGroupId(), status, paymentId, paymentDate));
    }
}