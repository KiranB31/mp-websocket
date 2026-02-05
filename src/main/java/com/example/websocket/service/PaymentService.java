package com.example.websocket.service;

import com.example.websocket.model.Order;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentService {

    public PaymentResponse processPayment(Order order, String paymentMethod, String vpa) {
        // MOCK UPI Payment Logic
        // In a real scenario, this would call PhonePe/Razorpay/Stripe API

        try {
            // Simulate network delay
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean success = true; // Mock success
        String txnId = "UPI-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        if (success) {
            return new PaymentResponse(true, txnId, "Payment Successful via " + paymentMethod);
        } else {
            return new PaymentResponse(false, null, "Payment Failed");
        }
    }

    public static class PaymentResponse {
        public boolean success;
        public String transactionId;
        public String message;

        public PaymentResponse(boolean success, String transactionId, String message) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
        }
    }
}
