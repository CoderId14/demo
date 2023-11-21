package com.example.demo.api.user.request;


import com.example.demo.Service.user.dto.request.SearchUserInput;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchUserRequest {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String role;
    private Boolean isActive;

    public SearchUserInput toInput() {
        return SearchUserInput.builder()
                .id(this.getId())
                .username(this.getUsername())
                .email(this.getEmail())
                .name(this.getName())
                .phoneNumber(this.getPhoneNumber())
                .address(this.getAddress())
                .role(this.getRole())
                .isActive(this.getIsActive())
                .build();
    }
}
