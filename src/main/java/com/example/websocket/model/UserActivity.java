package com.example.websocket.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String action; // E.g., "VIEW_PAGE", "ADD_TO_CART", "PLACE_ORDER"
    private String details; // E.g., "Visited Shop", "Added Headphones (x2) - $398.98"
    private String timestamp;
}
