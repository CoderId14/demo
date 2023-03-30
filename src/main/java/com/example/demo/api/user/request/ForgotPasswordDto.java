package com.example.demo.api.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForgotPasswordDto {
    @NotEmpty(message = "UsernameOrEmail not empty")
    private String usernameOrEmail;
}
