package com.redisrealtime.redis_realtime_api.controller;

import com.redisrealtime.redis_realtime_api.dto.OtpRequest;
import com.redisrealtime.redis_realtime_api.service.OtpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")  
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody OtpRequest request) {
        try {
            otpService.sendOtp(request.getPhoneNumber());
            return ResponseEntity.ok("OTP 전송 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OTP 전송 실패: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        try {
            boolean isValid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());
            return isValid
                    ? ResponseEntity.ok("OTP 인증 성공")
                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP 인증 실패: 잘못된 인증번호입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OTP 인증 중 오류 발생: " + e.getMessage());
        }
    }
}
