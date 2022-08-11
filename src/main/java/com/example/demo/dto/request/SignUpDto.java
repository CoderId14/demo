package com.example.demo.dto.request;


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
public class SignUpDto {
    @NotEmpty(message = "Email not empty")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email invalid")
    private String email;
    @NotEmpty(message = "Username not empty")
    @Pattern(regexp = "^(?=.{4,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$", message = "Username invalid")
    private String username;

    @Size(min = 4, max = 16, message = "Password must contain atleast 4 character and not exceed 16 character")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$", message = "Password invalid")
    private String password;
}