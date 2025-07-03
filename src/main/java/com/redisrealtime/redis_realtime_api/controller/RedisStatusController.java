package com.redisrealtime.redis_realtime_api.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisStatusController {

    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * Redis 서버가 응답하는지 확인하는 ping API
     * GET /api/redis/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<String> pingRedis() {
        try {
            RedisConnection connection = redisConnectionFactory.getConnection();
            String response = connection.ping();
            return ResponseEntity.ok("PONG 응답: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Redis 응답 실패: " + e.getMessage());
        }
    }

    /**
     * Redis 서버 연결을 시도하는 API
     * POST /api/redis/connect
     */
    @PostMapping("/connect")
    public ResponseEntity<String> connectRedis() {
        try {
            RedisConnection connection = redisConnectionFactory.getConnection();
            connection.ping(); 
            return ResponseEntity.ok("✅ Redis 연결 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Redis 연결 실패: " + e.getMessage());
        }
    }

    /**
     * Redis 키 목록 조회 API
     * GET /api/redis/keys
     */
    @GetMapping("/keys")
    public ResponseEntity<List<String>> getAllKeys() {
        try {
            RedisConnection connection = redisConnectionFactory.getConnection();
            Set<byte[]> keys = connection.keys("*".getBytes());
            List<String> keyList = keys.stream()
                                    .map(String::new)
                                    .collect(Collectors.toList());
            return ResponseEntity.ok(keyList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
