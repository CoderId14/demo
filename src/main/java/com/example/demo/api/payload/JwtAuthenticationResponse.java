package com.example.demo.api.payload;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String accessToken;
}
