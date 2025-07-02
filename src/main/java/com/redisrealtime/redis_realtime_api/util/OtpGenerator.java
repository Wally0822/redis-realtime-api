package com.redisrealtime.redis_realtime_api.util;

public class OtpGenerator {
    public static String generateOtp() {
        int otp = (int) (Math.random() * 9000) + 1000; 
        return String.valueOf(otp);
    }
}
