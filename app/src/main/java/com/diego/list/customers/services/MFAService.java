package com.diego.list.customers.services;

import com.diego.list.customers.model.User;
import com.diego.list.customers.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MFAService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Random random = new Random();
    private final RedisUtils redisUtils;

    public String generateMFACode(UUID userId) {
        String code = generateRandomCode(6);
        String mfaId = "mfa_" + System.currentTimeMillis();

        redisTemplate.opsForValue().set(
                "mfa_code:" + mfaId,
                userId + ":" + code,
                Duration.ofMinutes(5)
        );

        return mfaId;
    }

    public boolean verifyMFACode(String mfaId, String code) {
        String stored = (String) redisTemplate.opsForValue().get("mfa_code:" + mfaId);
        if (stored == null) {
            return false;
        }

        String[] parts = stored.split(":");
        if (parts.length != 2) {
            return false;
        }

        boolean valid = parts[1].equals(code);
        if (valid) {
            redisUtils.safeDel("mfa_code:" + mfaId);
        }

        return valid;
    }

    public void sendMFACode(User user, String mfaId) {
        String code = getMFACode(mfaId);
        log.info("MFA code send {}: {}", user.getEmail(), code);

    }

    private String getMFACode(String mfaId) {
        String stored = (String) redisTemplate.opsForValue().get("mfa_code:" + mfaId);
        return stored != null ? stored.split(":")[1] : "";
    }

    private String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
