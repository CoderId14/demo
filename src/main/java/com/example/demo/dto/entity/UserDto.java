package com.example.demo.dto.entity;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.supports.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private Long userId;

    @Email(message = "Email invalid")
    private String email;
    @NotEmpty(message = "Username not empty")
    private String username;

    private String name;
    private Boolean isActive;
    private String avatar;
    private Set<Role> roles;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date modifyDate;
}
