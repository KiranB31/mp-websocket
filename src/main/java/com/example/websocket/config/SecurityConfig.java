package com.example.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF
                .authorizeRequests()
                // Public Endpoints
                .antMatchers(
                        "/",
                        "/index.html",
                        "/user.html",
                        "/admin.html",
                        "/api/auth/**",
                        "/api/users/**",
                        "/api/products/**",
                        "/api/orders/**",
                        "/api/payment/**",
                        "/api/transactions/**",
                        "/api/config/**",
                        "/api/feedbacks/**",
                        "/ws/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/api/webhook/**", // Payment webhook must be public
                        "/error")
                .permitAll()
                // Protected Endpoints
                .anyRequest().authenticated()
                .and()
                .formLogin().disable() // We use custom JSON login
                .httpBasic().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}
