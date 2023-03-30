package com.example.demo.api.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}
