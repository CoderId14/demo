package com.example.demo.api.auth.response;

import com.example.demo.entity.supports.ERole;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtAuthenticationResponse {
    private String accessToken;

    private String refreshToken;

    private String username;

    private Set<ERole> role;

}
