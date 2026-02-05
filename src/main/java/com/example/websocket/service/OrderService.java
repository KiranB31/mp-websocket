package com.example.websocket.service;

import com.example.websocket.model.*;
import com.example.websocket.repository.OrderRepository;
import com.example.websocket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private com.example.websocket.repository.TransactionRepository transactionRepository; // Added Repository

    @Autowired
    private PaymentService paymentService;

    @Transactional
    public Order placeOrder(Order orderRequest, User user) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString().substring(0, 8));
        order.setUser(user);
        order.setCustomerName(orderRequest.getCustomerName());
        order.setAddress(orderRequest.getAddress());
        order.setPhone(orderRequest.getPhone());
        order.setStatus("PLACED");
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setTotalQuantity(orderRequest.getTotalQuantity());

        // In a real app we would map OrderItems here
        order.setProductSummary(orderRequest.getProductSummary());

        Order savedOrder = orderRepository.save(order);

        // Notify subscribers about the new order
        messagingTemplate.convertAndSend("/topic/orders", savedOrder);

        return savedOrder;
    }

    public Order updateStatus(String orderId, String status) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // Notify subscribers about the status change
        messagingTemplate.convertAndSend("/topic/orders", updatedOrder);
        messagingTemplate.convertAndSend("/topic/order/" + orderId, updatedOrder);

        return updatedOrder;
    }

    // Overloaded method to keep backward compatibility if needed, though we should
    // migrate all callers
    public Order processPayment(String orderId, String paymentMethod, String vpa, String transactionId) {
        log.info("Processing payment for Order ID: {} via {}", orderId, paymentMethod);
        Order order = getOrderById(orderId);

        // Mock Payment logic (if VPA is passed, it simulates client-side; if it's
        // webhook, we trust the caller)
        boolean isWebhook = "PHONEPE_WEBHOOK".equals(paymentMethod);

        if (isWebhook) {
            // Webhook means provider already confirmed it
            log.info("Payment confirmed via Webhook for Order: {}", orderId);
            order.setPaymentStatus("PAID");
            order.setPaymentMethod("UPI_WEBHOOK");
            order.setTransactionId(vpa); // using vpa param as txnId for webhook
            order.setStatus("PLACED");
        } else {
            // Standard mock flow
            PaymentService.PaymentResponse response = paymentService.processPayment(order, paymentMethod, vpa);
            if (response.success) {
                log.info("Payment Successful for Order: {}", orderId);

                // Validation 1: Check if already paid
                if ("PAID".equals(order.getPaymentStatus())) {
                    log.warn("Order {} is already paid. Ignoring duplicate payment.", orderId);
                    return order;
                }

                order.setPaymentStatus("PAID");
                order.setPaymentMethod(paymentMethod);
                // Use provided transactionId if available (e.g. from user input UTR), else
                // fallback to generated
                String finalTxnId = (transactionId != null && !transactionId.isEmpty()) ? transactionId
                        : response.transactionId;

                // Validation 2: Check for Duplicate Transaction ID
                if (transactionRepository.findByTransactionId(finalTxnId).isPresent()) {
                    throw new RuntimeException("Duplicate Transaction ID: " + finalTxnId);
                }

                order.setTransactionId(finalTxnId);
                order.setStatus("PLACED");

                // Save Transaction History
                com.example.websocket.model.Transaction txn = com.example.websocket.model.Transaction.builder()
                        .transactionId(finalTxnId)
                        .user(order.getUser())
                        .order(order)
                        .amount(order.getTotalAmount())
                        .status("SUCCESS")
                        .paymentMethod(paymentMethod)
                        .timestamp(java.time.LocalDateTime.now())
                        .build();
                transactionRepository.save(txn);

            } else {
                log.error("Payment Failed for Order: {}", orderId);
                order.setPaymentStatus("FAILED");

                // Save Failed Transaction
                com.example.websocket.model.Transaction txn = com.example.websocket.model.Transaction.builder()
                        .transactionId("FAILED-" + System.currentTimeMillis())
                        .user(order.getUser())
                        .order(order)
                        .amount(order.getTotalAmount())
                        .status("FAILED")
                        .paymentMethod(paymentMethod)
                        .timestamp(java.time.LocalDateTime.now())
                        .build();
                transactionRepository.save(txn);
            }
        }

        Order savedOrder = orderRepository.save(order);
        messagingTemplate.convertAndSend("/topic/orders", savedOrder);
        return savedOrder;
    }

    public Order refundOrder(String transactionId, String reason, String refundTxnId) {
        com.example.websocket.model.Transaction txn = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!"SUCCESS".equals(txn.getStatus())) {
            throw new RuntimeException("Cannot refund a failed or already refunded transaction");
        }

        Order order = txn.getOrder();

        // Validation: Cannot refund if shipped or delivered
        if ("SHIPPED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            throw new RuntimeException("Cannot refund order that is SHIPPED or DELIVERED. Please initiate a RETURN.");
        }

        txn.setStatus("REFUNDED");
        txn.setRefundReason(reason);
        txn.setRefundTransactionId(refundTxnId);
        transactionRepository.save(txn);

        order.setPaymentStatus("REFUNDED");
        order.setStatus("CANCELLED");
        order.setRefundReason(reason);
        order.setRefundTransactionId(refundTxnId);
        Order savedOrder = orderRepository.save(order);

        messagingTemplate.convertAndSend("/topic/orders", savedOrder);
        return savedOrder;
    }

    public Order processPayment(String orderId, String paymentMethod, String vpa) {
        return processPayment(orderId, paymentMethod, vpa, null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
