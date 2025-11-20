package com.diego.list.customers.repository;

import com.diego.list.customers.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUserIdAndDeviceFingerprint(UUID userId, String deviceFingerprint);

    @Query("SELECT d FROM Device d WHERE d.user.id = :userId AND d.trusted = true")
    List<Device> findTrustedDevicesByUserId(@Param("userId") UUID userId);

    long countByUserId(UUID userId);
}
