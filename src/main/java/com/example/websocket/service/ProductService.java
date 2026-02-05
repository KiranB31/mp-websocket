package com.example.websocket.service;

import com.example.websocket.model.Product;
import com.example.websocket.repository.ProductRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void seedProducts() {
        if (productRepository.count() == 0) {
            productRepository.save(Product.builder()
                    .name("Premium Wireless Headphones")
                    .price(199.99)
                    .category("Electronics")
                    .description("High-quality wireless headphones with noise cancellation.")
                    .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=200")
                    .isOnSale(true)
                    .discountPrice(119.99)
                    .build());
            productRepository.save(Product.builder()
                    .name("Smart Watch Series X")
                    .price(299.49)
                    .category("Electronics")
                    .description("Latest smart watch with health tracking features.")
                    .imageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=200")
                    .isOnSale(true)
                    .discountPrice(179.99)
                    .build());
            productRepository.save(Product.builder()
                    .name("Ergonomic Mechanical Keyboard")
                    .price(129.00)
                    .category("Accessories")
                    .description("Comfortable mechanical keyboard for long typing sessions.")
                    .imageUrl("https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=200")
                    .isOnSale(false)
                    .build());
            productRepository.save(Product.builder()
                    .name("Ultra Wide Monitor 34\"")
                    .price(449.99)
                    .category("Electronics")
                    .description("Immersive ultra-wide monitor for gaming and productivity.")
                    .imageUrl("https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=200")
                    .isOnSale(true)
                    .discountPrice(299.99)
                    .build());
            productRepository.save(Product.builder()
                    .name("Professional DSLR Camera")
                    .price(899.00)
                    .category("Electronics")
                    .description("Professional grade camera for high-quality photography.")
                    .imageUrl("https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=200")
                    .isOnSale(false)
                    .build());
            productRepository.save(Product.builder()
                    .name("Leather Messenger Bag")
                    .price(85.00)
                    .category("Fashion")
                    .description("Stylish and durable leather messenger bag.")
                    .imageUrl("https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=200")
                    .isOnSale(false)
                    .build());
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
