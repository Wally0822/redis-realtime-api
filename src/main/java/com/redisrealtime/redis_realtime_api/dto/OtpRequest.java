package com.redisrealtime.redis_realtime_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequest {
    @NotBlank
    private String phoneNumber;
    private String otp;
}