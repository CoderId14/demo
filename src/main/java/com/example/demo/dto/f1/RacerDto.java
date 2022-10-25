package com.example.demo.dto.f1;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RacerDto {
    private Long id;
    private String name;
    private Date dateOfBirth;
    private String bio;
    private String national;
}
