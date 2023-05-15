package com.example.demo.api.auth.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SignUpRequest {
    @NotEmpty(message = "Email not empty")
    private String email;
    @NotEmpty(message = "Username not empty")
    private String username;

    private String name;
    @Size(min = 4, max = 16, message = "Password must contain atleast 4 character and not exceed 16 character")
    private String password;
}