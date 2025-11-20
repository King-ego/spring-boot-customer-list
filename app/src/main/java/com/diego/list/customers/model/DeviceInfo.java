package com.diego.list.customers.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo implements Serializable {
    private String browser;
    private String os;
    private String screenResolution;
    private String timezone;
    private String language;
    private String country;
    private String city;
    private boolean isMobile;
    private boolean isTablet;
    private boolean isDesktop;
}