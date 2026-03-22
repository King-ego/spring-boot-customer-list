package com.diego.list.customers.redis;

import com.diego.list.customers.model.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRedisRepository extends CrudRepository<Session, String> {
    Optional<Session> findBySessionId(String sessionId);
    List<Session> findByUserId(UUID userId);
    List<Session> findByUserIdAndIsActive(String userId, boolean isActive);
    long countByUserIdAndIsActive(String userId, boolean isActive);
}