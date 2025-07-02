package com.redisrealtime.redis_realtime_api.controller;

import com.redisrealtime.redis_realtime_api.dto.OtpRequest;
import com.redisrealtime.redis_realtime_api.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")  
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        otpService.sendOtp(request.getPhoneNumber());
        return ResponseEntity.ok("OTP sent successfully.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());
        return isValid
                ? ResponseEntity.ok("OTP verified.")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
    }
}
