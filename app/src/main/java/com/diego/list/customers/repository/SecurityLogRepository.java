package com.diego.list.customers.repository;

import com.diego.list.customers.model.SecurityEventType;
import com.diego.list.customers.model.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {

    @Query("SELECT COUNT(sl) FROM SecurityLog sl WHERE sl.userId = :userId AND sl.eventType = 'LOGIN_FAILURE' AND sl.timestamp > :since")
    long countRecentFailures(@Param("userId") String userId, @Param("since") LocalDateTime since);

    List<SecurityLog> findByUserIdOrderByTimestampDesc(String userId);
    List<SecurityLog> findByEventTypeAndTimestampAfter(SecurityEventType eventType, LocalDateTime timestamp);

    @Query("SELECT sl FROM SecurityLog sl WHERE sl.timestamp >= :startDate ORDER BY sl.timestamp DESC")
    List<SecurityLog> findRecentLogs(@Param("startDate") LocalDateTime startDate);
}
