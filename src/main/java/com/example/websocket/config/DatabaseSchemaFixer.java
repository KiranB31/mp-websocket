package com.example.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DatabaseSchemaFixer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Checking database schema for 'transactions' table...");
            // Force user_id to be nullable in transactions table
            jdbcTemplate.execute("ALTER TABLE transactions MODIFY user_id BIGINT NULL;");
            log.info("Database schema updated: 'transactions.user_id' is now nullable.");
        } catch (Exception e) {
            log.warn(
                    "Could not alter table transactions: {}. This might be normal if the column is already nullable or if using a different DB.",
                    e.getMessage());
        }

        try {
            // Also check for 'orders' table just in case
            jdbcTemplate.execute("ALTER TABLE orders MODIFY user_id BIGINT NULL;");
            log.info("Database schema updated: 'orders.user_id' is now nullable.");
        } catch (Exception e) {
            log.warn("Could not alter table orders: {}", e.getMessage());
        }
    }
}
