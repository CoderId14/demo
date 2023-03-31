package com.example.demo.api.user.response;


import com.example.demo.entity.supports.ERole;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long userId;

    @Email(message = "Email invalid")
    private String email;
    @NotEmpty(message = "Username not empty")
    private String username;

    private String name;
    private Boolean isActive;
    private String avatar;
    private Set<ERole> roles;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
