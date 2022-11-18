package com.example.demo.dto.f1;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class GrandPrixDto {
    private Long id;
    private String name;
    private int laps;
    private LocalDateTime time;
    private String description;
    private String raceCourse;
    private String season;
}
