package com.diego.list.customers.repository;

import com.diego.list.customers.model.MethodPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MethodPaymentRepository extends JpaRepository<MethodPayment, UUID> {
}


