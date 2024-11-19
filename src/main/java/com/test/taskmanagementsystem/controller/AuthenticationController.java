package com.test.taskmanagementsystem.controller;

import com.test.taskmanagementsystem.model.dto.request.RefreshRequest;
import com.test.taskmanagementsystem.model.dto.request.SignInRequest;
import com.test.taskmanagementsystem.model.dto.response.JwtAuthenticationResponse;
import com.test.taskmanagementsystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signing(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signing(request));
    }

    @PostMapping("/refresh/access")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

}
