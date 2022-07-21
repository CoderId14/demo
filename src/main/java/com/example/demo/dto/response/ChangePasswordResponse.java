package com.example.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChangePasswordResponse {
    private String username;
    private String password;
    private String token;
}
