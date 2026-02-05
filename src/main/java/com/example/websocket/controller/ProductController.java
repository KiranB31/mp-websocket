package com.example.websocket.controller;

import com.example.websocket.model.Product;
import com.example.websocket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

        @Autowired
        private ProductService productService;

        @GetMapping
        public List<Product> getProducts(@RequestParam(required = false) String query) {
                if (query == null || query.isEmpty()) {
                        return productService.getAllProducts();
                }
                return productService.searchProducts(query);
        }

        @PostMapping
        public Product addProduct(@RequestBody Product product) {
                return productService.saveProduct(product);
        }

        @DeleteMapping("/{id}")
        public void deleteProduct(@PathVariable Long id) {
                productService.deleteProduct(id);
        }
}
