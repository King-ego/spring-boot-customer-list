package com.diego.list.customers.repository;

import com.diego.list.customers.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("UPDATE Order o SET o.status = :status, o.paymentId = :paymentId, o.paymentDate = :paymentDate WHERE o.id = :orderId")
    public void updateParse(UUID orderId, String status, String paymentId, LocalDateTime paymentDate);
}