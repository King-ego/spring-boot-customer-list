package com.diego.list.customers.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("Session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue
    private UUID sessionId;

    private UUID userId;

    private String deviceFingerprint;

    private String ipAddress;

    private String userAgent;

    @Embedded
    private DeviceInfo deviceInfo;

}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
class DeviceInfo {
    private String browser;
    private String os;
    private String screenResolution;
    private String timezone;
    private String language;
    private String country;
    private String city;
    private Boolean isMobile;
    private Boolean isTablet;
    private Boolean isDesktop;
}