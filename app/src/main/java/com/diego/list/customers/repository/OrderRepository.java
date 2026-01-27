package com.diego.list.customers.repository;

import com.diego.list.customers.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Modifying
    @Query("UPDATE Order o SET o.status = :status, o.paymentId = :paymentId, o.paymentDate = :paymentDate WHERE o.groupId = :groupId")
    void updateParse(UUID groupId, String status, String paymentId, LocalDateTime paymentDate);

    @Query("SELECT o FROM Order o WHERE o.groupId = :groupId")
    List <Order> findAllGroupId(UUID groupId);
}