package com.example.websocket.controller;

import com.example.websocket.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
@Slf4j
public class WebhookController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/phonepe")
    public ResponseEntity<?> handlePhonePeCallback(@RequestBody Map<String, Object> payload) {
        log.info("Received Payment Webhook: {}", payload);

        try {
            // Simulate PhonePe payload structure
            // { "orderId": "...", "code": "PAYMENT_SUCCESS", "transactionId": "..." }

            String orderId = (String) payload.get("orderId");
            String code = (String) payload.get("code");
            String txnId = (String) payload.get("transactionId");

            log.info("Processing Webhook for Order: {}, Status: {}", orderId, code);

            if ("PAYMENT_SUCCESS".equals(code)) {
                orderService.processPayment(orderId, "PHONEPE_WEBHOOK", txnId);
                log.info("Order {} updated recursively to PAID via Webhook", orderId);
                return ResponseEntity.ok(java.util.Collections.singletonMap("status", "accepted"));
            } else {
                log.warn("Payment Failed for Order: {}", orderId);
                java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
                errorResponse.put("status", "failed");
                errorResponse.put("reason", "Payment not successful");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
