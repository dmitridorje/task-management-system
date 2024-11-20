package com.test.taskmanagementsystem.service;

import com.test.taskmanagementsystem.model.dto.jwt.request.RefreshRequest;
import com.test.taskmanagementsystem.model.dto.jwt.request.SignInRequest;
import com.test.taskmanagementsystem.model.dto.jwt.response.JwtAuthenticationResponse;

public interface AuthenticationService {

    JwtAuthenticationResponse signing(SignInRequest request);

    JwtAuthenticationResponse refreshToken(RefreshRequest request);
}
