package com.example.demo.Service.user.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchUserInput {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String role;
    private Boolean isActive;
}
