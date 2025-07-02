package com.redisrealtime.redis_realtime_api.service;

import com.redisrealtime.redis_realtime_api.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {
    public final StringRedisTemplate redisTemplate;
    private static final long EXPIRE_TIME = 180;

    public void sendOtp(String phoneNumber) {
        String otp = OtpGenerator.generateOtp();
        String key = "OTP:" + phoneNumber;
        redisTemplate.opsForValue().set(key, otp, EXPIRE_TIME, TimeUnit.SECONDS);
        System.out.println("📲 OTP for " + phoneNumber + ": " + otp);
    }

    public boolean verifyOtp(String phoneNumber, String inputOtp) {
        String key = "OTP:" + phoneNumber;
        String storedOtp = redisTemplate.opsForValue().get(key);
        return storedOtp != null && storedOtp.equals(inputOtp);
    }
}