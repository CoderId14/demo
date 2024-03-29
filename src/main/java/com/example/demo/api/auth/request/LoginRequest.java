package com.example.demo.api.auth.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LoginRequest {
    @NotEmpty(message = "Username must not empty")
    @Pattern(regexp = "^(?=.{4,30}$)(?![_.])[a-zA-Z0-9._@]+(?<![_.])$", message = "Username invalid")
    private String username;
    @NotEmpty(message = "Password must not empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$", message = "Password invalid")
    private String password;
}
