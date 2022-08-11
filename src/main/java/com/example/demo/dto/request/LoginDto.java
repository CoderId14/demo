package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.regex.Matcher;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LoginDto {
    @NotEmpty(message = "Username not empty")
    @Pattern(regexp = "^(?=.{4,30}$)(?![_.])[a-zA-Z0-9._@]+(?<![_.])$", message = "Username invalid")
    private String username;
    @NotEmpty(message = "Password not empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$", message = "Password invalid")
    private String password;
}
