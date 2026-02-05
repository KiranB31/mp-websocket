package com.example.websocket.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId; // Keep for display/tracking purposes

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String customerName;
    private String address;
    private String phone;

    @Column(length = 1000)
    private String productSummary; // Summary of products

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    private double totalAmount;
    private int totalQuantity;

    private String status; // PLACED, PROCESSING, SHIPPED, DELIVERED

    private String paymentStatus; // PENDING, PAID, FAILED
    private String paymentMethod; // UPI, COD
    private String transactionId;
    private String refundReason;
    private String refundTransactionId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
