package com.diego.list.customers.repository;

import com.diego.list.customers.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUserIdAndDeviceFingerprint(UUID userId, String deviceFingerprint);
}
