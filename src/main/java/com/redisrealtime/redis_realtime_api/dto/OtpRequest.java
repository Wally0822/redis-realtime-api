package com.redisrealtime.redis_realtime_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequest {
    private String phoneNumber;
    private String otp;
}