package com.diego.list.customers.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class KeyInRedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void punishKey(String key, Object value, Duration duration) {
        try {
            String serialized = objectMapper.writeValueAsString(value);
            if (duration == null) {
                redisTemplate.opsForValue().set(key, serialized);
            } else {
                redisTemplate.opsForValue().set(key, serialized, duration);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Generate key error", e);
        }
    }
}
