package com.example.demo.dto.request;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CheckEmail {
    @NotEmpty
    private String email;
}
