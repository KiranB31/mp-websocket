package com.example.websocket.controller;

import com.example.websocket.model.User;
import com.example.websocket.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody com.example.websocket.dto.RegisterRequest request) {
        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .role(User.Role.USER) // Default role
                    .mfaEnabled(request.isMfaEnabled())
                    .build();

            return ResponseEntity.ok(authService.register(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(java.util.Collections.singletonMap("message", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        return authService.login(credentials.get("username"), credentials.get("password"))
                .map(user -> {
                    if (user.isMfaEnabled()) {
                        authService.initiateMfa(user, "EMAIL"); // Default to email for now
                        java.util.Map<String, Object> mfaResponse = new java.util.HashMap<>();
                        mfaResponse.put("status", "MFA_REQUIRED");
                        mfaResponse.put("username", user.getUsername());
                        return ResponseEntity.ok(mfaResponse);
                    }
                    java.util.Map<String, Object> successResponse = new java.util.HashMap<>();
                    successResponse.put("status", "SUCCESS");
                    successResponse.put("user", user);
                    return ResponseEntity.ok(successResponse);
                })
                .orElse(ResponseEntity.status(401)
                        .body(java.util.Collections.singletonMap("message", "Invalid credentials")));
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<?> verifyMfa(@RequestBody Map<String, String> request) {
        // In a real app we would use a more secure lookup
        return ResponseEntity.ok(java.util.Collections.singletonMap("message", "MFA verified (Mock)"));
    }
}
