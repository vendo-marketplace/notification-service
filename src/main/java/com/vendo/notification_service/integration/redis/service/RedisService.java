package com.vendo.notification_service.integration.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveValue(String key, String value, long seconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(seconds));
    }

    public Optional<String> getValue(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}