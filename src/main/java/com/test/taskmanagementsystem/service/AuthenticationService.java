package com.test.taskmanagementsystem.service;

import com.test.taskmanagementsystem.model.dto.request.RefreshRequest;
import com.test.taskmanagementsystem.model.dto.request.SignInRequest;
import com.test.taskmanagementsystem.model.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signing(SignInRequest request);

    JwtAuthenticationResponse refreshToken(RefreshRequest request);
}
