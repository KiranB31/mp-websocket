package com.example.websocket.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId; // UTR or External ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne // One order can have multiple transaction attempts (e.g. failed then success)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Double amount;

    private String status; // SUCCESS, FAILED, REFUNDED, PENDING

    private String paymentMethod; // UPI, CARD, etc.

    private String refundReason;
    private String refundTransactionId; // The UTR of the refund back to the user

    private LocalDateTime timestamp;
}
