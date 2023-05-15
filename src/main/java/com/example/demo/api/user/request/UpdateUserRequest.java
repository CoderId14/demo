package com.example.demo.api.user.request;

import com.example.demo.entity.supports.ERole;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class UpdateUserRequest {
    @NotNull
    private Long userId;
    @NotNull
    private String name;
    private Boolean isActive;
    private String avatar;
    @NotNull
    private Set<ERole> roles;
}
