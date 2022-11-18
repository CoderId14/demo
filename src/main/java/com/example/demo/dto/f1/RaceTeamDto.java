package com.example.demo.dto.f1;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RaceTeamDto {
    private long id;
    private String name;
    private float powerUnit;
    private String description;
    private String season;
}
