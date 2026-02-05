package com.example.websocket.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MockMfaService {

    public void sendOtp(String recipient, String otp, String type) {
        log.info("Sending {} OTP [{}] to: {}", type, otp, recipient);
        System.out.println(">>> [MOCK " + type + "] Sending OTP [" + otp + "] to: " + recipient);
    }
}
