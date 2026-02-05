package com.example.websocket.repository;

import com.example.websocket.model.MfaToken;
import com.example.websocket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MfaTokenRepository extends JpaRepository<MfaToken, Long> {
    Optional<MfaToken> findByUserAndToken(User user, String token);

    void deleteByUser(User user);
}
