package com.example.websocket.controller;

import com.example.websocket.model.Order;
import com.example.websocket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/process")
    public Order processPayment(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId");
        String method = payload.get("method");
        String vpa = payload.get("vpa");
        String transactionId = payload.get("transactionId");

        // Passing transactionId to OrderService
        return orderService.processPayment(orderId, method, vpa, transactionId);
    }
}
