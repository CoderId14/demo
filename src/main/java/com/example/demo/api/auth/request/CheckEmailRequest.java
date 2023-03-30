package com.example.demo.api.auth.request;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CheckEmailRequest {
    @NotEmpty
    private String email;
}
