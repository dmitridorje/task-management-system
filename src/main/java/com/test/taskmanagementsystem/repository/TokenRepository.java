package com.test.taskmanagementsystem.repository;

import com.test.taskmanagementsystem.model.entity.Token;
import com.test.taskmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    Optional<List<Token>> findAllByUserAndIsActive(User user, Boolean isActive);

    Optional<Token> findByRefreshToken(String refreshToken);
}
