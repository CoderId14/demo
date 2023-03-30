package com.example.demo.Service.user;

import com.example.demo.api.auth.request.LoginRequest;
import com.example.demo.api.auth.request.SignUpRequest;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.api.auth.request.TokenRefreshRequest;
import com.example.demo.api.auth.response.JwtAuthenticationResponse;
import com.example.demo.api.auth.response.TokenRefreshResponse;

public interface IUserService {
    UserDto signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse login(LoginRequest loginRequest);
    TokenRefreshResponse refreshToken(TokenRefreshRequest tokenRefreshRequest);
}
