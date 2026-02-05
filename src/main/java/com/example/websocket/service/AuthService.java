package com.example.websocket.service;

import com.example.websocket.model.MfaToken;
import com.example.websocket.model.User;
import com.example.websocket.repository.MfaTokenRepository;
import com.example.websocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MfaTokenRepository mfaTokenRepository;

    @Autowired
    private MockMfaService mfaService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + user.getUsername() + "' is already taken");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email '" + user.getEmail() + "' is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Transactional
    public void initiateMfa(User user, String type) {
        String token = String.format("%06d", new Random().nextInt(999999));

        mfaTokenRepository.deleteByUser(user);

        MfaToken mfaToken = MfaToken.builder()
                .user(user)
                .token(token)
                .type(type)
                .expiryAt(LocalDateTime.now().plusMinutes(5))
                .build();

        mfaTokenRepository.save(mfaToken);

        String recipient = type.equals("EMAIL") ? user.getEmail() : user.getPhone();
        mfaService.sendOtp(recipient, token, type);
    }

    public boolean verifyMfa(User user, String token) {
        return mfaTokenRepository.findByUserAndToken(user, token)
                .map(mfa -> {
                    boolean valid = mfa.getExpiryAt().isAfter(LocalDateTime.now());
                    if (valid) {
                        mfaTokenRepository.delete(mfa);
                    }
                    return valid;
                })
                .orElse(false);
    }
}
