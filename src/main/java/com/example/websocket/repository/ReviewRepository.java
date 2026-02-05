package com.example.websocket.repository;

import com.example.websocket.model.Review;
import com.example.websocket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
}
