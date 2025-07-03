package com.redisrealtime.redis_realtime_api.service;

import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {
    
    private final StringRedisTemplate redisTemplate;

    public void addOrUpdateScore(String userId, double score) {
        redisTemplate.opsForZSet().add("ranking", userId, score);
    }

    public Set<ZSetOperations.TypedTuple<String>> getTopRankers(int count) {
        return redisTemplate.opsForZSet().reverseRangeWithScores("ranking", 0, count - 1);
    }

    public Long getUserRank(String userId) {
        return redisTemplate.opsForZSet().reverseRank("ranking", userId);
    }

    public Double getUserScore(String userId) {
        return redisTemplate.opsForZSet().score("ranking", userId);
    }
}
