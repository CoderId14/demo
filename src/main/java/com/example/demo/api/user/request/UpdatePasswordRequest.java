package com.example.demo.api.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePasswordRequest {
    @NotEmpty
    private String usernameOrEmail;
    @NotEmpty
    @Size(min = 4, max = 50)
    private String currentPassword;
    @NotEmpty
    @Size(min = 4, max = 50)
    private String newPassword;
}
