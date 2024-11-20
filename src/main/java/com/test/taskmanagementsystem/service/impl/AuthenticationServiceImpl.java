package com.test.taskmanagementsystem.service.impl;

import com.test.taskmanagementsystem.exception.RefreshTokenNotFoundException;
import com.test.taskmanagementsystem.model.dto.jwt.request.RefreshRequest;
import com.test.taskmanagementsystem.model.dto.jwt.request.SignInRequest;
import com.test.taskmanagementsystem.model.dto.jwt.response.JwtAuthenticationResponse;
import com.test.taskmanagementsystem.model.entity.Token;
import com.test.taskmanagementsystem.repository.TokenRepository;
import com.test.taskmanagementsystem.repository.UserRepository;
import com.test.taskmanagementsystem.service.AuthenticationService;
import com.test.taskmanagementsystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Value("${token.access.token.expiration}")
    private long accessTokenExpiration;

    @Transactional
    @Override
    public JwtAuthenticationResponse signing(SignInRequest request) {
        log.info("Starting login process for username: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            log.info("Authentication successful for username: {}", request.getUsername());
        } catch (Exception e) {
            log.error("Authentication failed for username: {}. Error: {}", request.getUsername(), e.getMessage());
            throw new IllegalArgumentException("Invalid username or password.");
        }

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.error("User with username {} not found.", request.getUsername());
                    return new IllegalArgumentException("Invalid username or password.");
                });
        log.info("Found user: {}", user.getUsername());

        List<Token> activeTokens = tokenRepository.findAllByUserAndIsActive(user, true).orElse(Collections.emptyList());
        if (!activeTokens.isEmpty()) {
            log.info("Deactivating {} active token(s) for user {}", activeTokens.size(), user.getUsername());
            for (Token activeToken : activeTokens) {
                activeToken.setIsActive(false);
            }
            tokenRepository.saveAll(activeTokens);
        } else {
            log.info("No active tokens found for user {}", user.getUsername());
        }

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var expiresAt = new Date(System.currentTimeMillis() + accessTokenExpiration);

        tokenRepository.save(new Token(jwt, refreshToken, expiresAt, user, true));

        log.info("Generated new JWT for user {} with expiration time: {}", user.getUsername(), expiresAt);

        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt)
                .build();
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshRequest request) {
        log.info("Starting refresh token process for refresh token: {}", request.getRefreshToken());

        Token token = tokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> {
                    log.error("Refresh token not found: {}", request.getRefreshToken());
                    return new RefreshTokenNotFoundException(request.getRefreshToken());
                });

        log.info("Refresh token found for user: {}", token.getUser().getUsername());

        if (token.getExpiresAt().after(new Date()) && token.getIsActive()) {
            log.info("Refresh token is valid and active. Generating new tokens.");

            var user = token.getUser();
            var newAccessToken = jwtService.generateToken(user);
            var newRefreshToken = jwtService.generateRefreshToken(user);
            var expiresAt = new Date(System.currentTimeMillis() + accessTokenExpiration);

            token.setIsActive(false);
            tokenRepository.save(token);
            tokenRepository.save(new Token(newAccessToken, newRefreshToken, expiresAt, user, true));

            log.info("Generated new access token and refresh token for user {} with expiration time: {}", user.getUsername(), expiresAt);

            return new JwtAuthenticationResponse(newAccessToken, newRefreshToken, expiresAt);
        } else {
            log.warn("Refresh token is either expired or inactive. Deleting token.");
            tokenRepository.delete(token);
            return null;
        }
    }
}
