package com.example.websocket.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${app.payment.admin-vpa}")
    private String adminVpa;

    @GetMapping("/vpa")
    public ResponseEntity<?> getAdminVpa() {
        return ResponseEntity.ok(java.util.Collections.singletonMap("vpa", adminVpa));
    }
}
