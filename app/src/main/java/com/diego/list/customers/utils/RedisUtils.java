package com.diego.list.customers.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;


    public void safeDel( String keys) {
        try {
            Long deleted = redisTemplate.delete(java.util.Arrays.asList(keys));
            log.debug("Chaves deletadas: {} resultado: {}", keys, deleted);
        } catch (Exception e) {
            log.warn("Erro ao deletar chaves {}: {}", keys, e.getMessage());
        }
    }
}