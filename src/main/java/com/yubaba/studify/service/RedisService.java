package com.yubaba.studify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    // 이메일 인증
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

    // 이메일 인증 완료 상태 관리
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    // refresh Token 관리
    public void setRefreshToken(String email, String refreshToken, long expireSeconds) {
        redisTemplate.opsForValue().set("refresh:" + email, refreshToken, Duration.ofSeconds(expireSeconds));
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get("refresh:" + email);
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete("refresh:" + email);
    }
}
