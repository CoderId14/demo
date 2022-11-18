package com.example.demo.dto.f1;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RacerOfTeamDto {
    private long racerId;
    private long raceTeamId;
    private String racerName;
    private String raceTeamName;
}
