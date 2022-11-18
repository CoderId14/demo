package com.example.demo.dto.f1;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SeasonDto {
    private long id;
    private String name;
}
