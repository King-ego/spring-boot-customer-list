package com.diego.list.customers.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public void safeDel( String keys) {
        try {
            Long deleted = redisTemplate.delete(Collections.singletonList(keys));
            log.debug("Key Deleted: {} result: {}", keys, deleted);
        } catch (Exception e) {
            log.warn("Erro in delete {}: {}", keys, e.getMessage());
        }
    }
}
