package com.example.demo.dto;


import com.example.demo.entity.Role;
import com.example.demo.entity.user.User;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.api.user.response.UserResponse;
import com.example.demo.api.auth.response.JwtAuthenticationResponse;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class Mapper {
    public static UserResponse toUserDto(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                .name(user.getName())
                .isActive(user.getIsActive())
                .createDate(user.getCreatedDate())
                .modifyDate(user.getModifiedDate())
                .build();
    }

    public static JwtAuthenticationResponse toJwtAuthenticationRepsonse(String token, CustomUserDetails user, String refreshToken) {
        return JwtAuthenticationResponse.builder()
                .accessToken(token)
                .username(user.getUsername())
                .refreshToken(refreshToken)
                .role(user.getUser().getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                .build();
    }

}
