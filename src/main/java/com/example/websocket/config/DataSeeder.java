package com.example.websocket.config;

import com.example.websocket.model.Product;
import com.example.websocket.model.User;
import com.example.websocket.repository.ProductRepository;
import com.example.websocket.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

        @Bean
        CommandLineRunner initDatabase(ProductRepository productRepository, UserRepository userRepository) {
                return args -> {

                        // Seed Users
                        // Static users removed as per requirements.
                        // Users must register via the signup flow.
                        /*
                         * if (userRepository.count() == 0) {
                         * User admin = User.builder()
                         * .username("admin")
                         * .password(passwordEncoder.encode("admin123"))
                         * .email("admin@example.com")
                         * .role(User.Role.ADMIN)
                         * .build();
                         * userRepository.save(admin);
                         * 
                         * User user = User.builder()
                         * .username("user")
                         * .password(passwordEncoder.encode("user123"))
                         * .email("user@example.com")
                         * .role(User.Role.USER)
                         * .build();
                         * userRepository.save(user);
                         * 
                         * System.out.println("Users Seeded");
                         * }
                         */

                        // Seed Products
                        if (productRepository.count() == 0) {
                                List<Product> products = java.util.Arrays.asList(
                                                Product.builder()
                                                                .name("Cheese Pizza")
                                                                .description("Delicious cheese pizza with extra toppings")
                                                                .price(12.99)
                                                                .category("Pizza")
                                                                .imageUrl("https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500")
                                                                .build(),
                                                Product.builder()
                                                                .name("Veggie Burger")
                                                                .description("Healthy veggie burger with fresh lettuce")
                                                                .price(8.50)
                                                                .category("Burger")
                                                                .imageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500")
                                                                .build(),
                                                Product.builder()
                                                                .name("Chicken Biryani")
                                                                .description("Authentic Hyderabadi Chicken Biryani")
                                                                .price(15.00)
                                                                .category("Biryani")
                                                                .imageUrl("https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500")
                                                                .build(),
                                                Product.builder()
                                                                .name("Chocolate Shake")
                                                                .description("Thick and creamy chocolate milkshake")
                                                                .price(5.00)
                                                                .category("Drinks")
                                                                .imageUrl("https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=500")
                                                                .build(),
                                                Product.builder()
                                                                .name("Pasta Alfredo")
                                                                .description("Creamy Alfredo pasta with mushrooms")
                                                                .price(13.50)
                                                                .category("Pasta")
                                                                .imageUrl("https://images.unsplash.com/photo-1626844131082-256783844137?w=500")
                                                                .build(),
                                                Product.builder()
                                                                .name("Tandoori Chicken")
                                                                .description("Spicy grilled tandoori chicken")
                                                                .price(18.00)
                                                                .category("Starters")
                                                                .imageUrl("https://images.unsplash.com/photo-1628294895950-98052523e036?w=500")
                                                                .build());

                                productRepository.saveAll(products);
                                System.out.println("Products Seeded");
                        }
                };
        }
}
