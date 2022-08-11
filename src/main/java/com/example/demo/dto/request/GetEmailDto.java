package com.example.demo.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class GetEmailDto {
    @NotEmpty(message = "usernameOrEmail must not empty")
    private String usernameOrEmail;
}
