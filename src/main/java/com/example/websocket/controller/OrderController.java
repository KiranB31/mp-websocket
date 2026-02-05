package com.example.websocket.controller;

import com.example.websocket.model.Order;
import com.example.websocket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private com.example.websocket.repository.UserRepository userRepository;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order) {
        // Find user by customerName (contains username from frontend)
        System.out.println("Placing order for customer: " + order.getCustomerName());
        com.example.websocket.model.User user = userRepository.findByUsername(order.getCustomerName())
                .orElse(null);

        if (user == null) {
            System.out.println("Warning: User not found for username: " + order.getCustomerName());
        } else {
            System.out.println("Order linked to user ID: " + user.getId());
        }

        // Initial state before payment
        order.setStatus("PENDING_PAYMENT");
        order.setPaymentStatus("PENDING");
        return orderService.placeOrder(order, user);
    }

    // Payment logic moved to PaymentController

    @PutMapping("/{orderId}/status")
    public Order updateStatus(@PathVariable String orderId, @RequestParam String status) {
        return orderService.updateStatus(orderId, status);
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
