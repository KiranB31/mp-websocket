package com.example.websocket.repository;

import com.example.websocket.model.Order;
import com.example.websocket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    Optional<Order> findByOrderId(String orderId);
}
