package com.diego.list.customers.repository;

import com.diego.list.customers.model.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {

    @Query("SELECT COUNT(sl) FROM SecurityLog sl WHERE sl.userId = :userId AND sl.eventType = com.diego.list.customers.model.SecurityEventType.LOGIN_FAILURE AND sl.localTimestamp > :since")
    long countRecentFailures(@Param("userId") UUID userId, @Param("since") LocalDateTime since);

    @Query("SELECT sl FROM SecurityLog sl WHERE sl.localTimestamp >= :startDate ORDER BY sl.localTimestamp DESC")
    List<SecurityLog> findRecentLogs(@Param("startDate") LocalDateTime startDate);
}
