package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordDto {
    @NotEmpty(message = "UsernameOrEmail not empty")
    private String usernameOrEmail;
    @NotEmpty(message = "Password not empty")
    private String password;

    private String token;
}
