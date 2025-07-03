package com.redisrealtime.redis_realtime_api.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RankingRequest {
    private String userId;
    private double score;
}
