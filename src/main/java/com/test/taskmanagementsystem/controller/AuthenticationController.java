package com.test.taskmanagementsystem.controller;

import com.test.taskmanagementsystem.model.dto.request.RefreshRequest;
import com.test.taskmanagementsystem.model.dto.request.SignInRequest;
import com.test.taskmanagementsystem.model.dto.response.JwtAuthenticationResponse;
import com.test.taskmanagementsystem.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to user authentication and token management")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Sign in",
            description = "Authenticates a user and provides a JWT access token.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtAuthenticationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signing(
            @RequestBody
            @Parameter(description = "User credentials for authentication", required = true)
            SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signing(request));
    }

    @Operation(
            summary = "Refresh access token",
            description = "Refreshes the JWT access token using a valid refresh token.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access token refreshed successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtAuthenticationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid or expired refresh token",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/refresh/access")
    public ResponseEntity<JwtAuthenticationResponse> refresh(
            @RequestBody
            @Parameter(description = "Valid refresh token to generate a new access token", required = true)
            RefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
