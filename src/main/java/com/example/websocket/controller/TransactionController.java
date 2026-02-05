package com.example.websocket.controller;

import com.example.websocket.model.Order;
import com.example.websocket.model.Transaction;
import com.example.websocket.repository.TransactionRepository;
import com.example.websocket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @PostMapping("/{transactionId}/refund")
    public ResponseEntity<Order> refundTransaction(
            @PathVariable String transactionId,
            @RequestParam String reason,
            @RequestParam String refundTxnId) {
        try {
            Order refundedOrder = orderService.refundOrder(transactionId, reason, refundTxnId);
            return ResponseEntity.ok(refundedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
