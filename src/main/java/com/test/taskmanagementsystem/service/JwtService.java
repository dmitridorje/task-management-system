package com.test.taskmanagementsystem.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    List<String> extractRoles(String token);
}
