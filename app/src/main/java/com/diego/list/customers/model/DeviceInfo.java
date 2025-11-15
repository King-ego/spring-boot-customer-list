package com.diego.list.customers.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {
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