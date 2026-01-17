package com.diego.list.customers.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Slf4j
public class DeviceFingerprintService {

    public String generateFingerprint(HttpServletRequest request) {
        StringBuilder fingerprintData = new StringBuilder();

        fingerprintData.append(request.getHeader("User-Agent"));
        fingerprintData.append(request.getHeader("Accept-Language"));
        fingerprintData.append(request.getHeader("Accept-Encoding"));
        fingerprintData.append(request.getHeader("Accept-Charset"));
        fingerprintData.append(request.getHeader("Screen-Resolution"));
        fingerprintData.append(request.getHeader("Timezone"));
        fingerprintData.append(getClientIP(request));

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(fingerprintData.toString().getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("Generating fingerprint failed, falling back to simple hashCode", e);
            return String.valueOf(fingerprintData.toString().hashCode());
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
