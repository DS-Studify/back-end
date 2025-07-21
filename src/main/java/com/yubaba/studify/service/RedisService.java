package com.yubaba.studify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void setEmailAuthCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(5)); // TTL 5분
    }

    public String getEmailAuthCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void deleteEmailAuthCode(String email) {
        redisTemplate.delete(email);
    }

    public void replaceEmailAuthCode(String email, String newCode) {
        redisTemplate.opsForValue().set(email, newCode, Duration.ofMinutes(5));
    }
}
