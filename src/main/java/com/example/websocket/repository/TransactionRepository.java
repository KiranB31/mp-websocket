package com.example.websocket.repository;

import com.example.websocket.model.Transaction;
import com.example.websocket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);

    List<Transaction> findByUser(User user);

    List<Transaction> findByStatus(String status);
}
