package com.diego.list.customers.repository;

import com.diego.list.customers.model.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
    Optional<Session> findBySessionId(String sessionId);
    List<Session> findByUserId(String userId);
    List<Session> findByUserIdAndIsActive(String userId, boolean isActive);
    long countByUserIdAndIsActive(String userId, boolean isActive);
}
