package com.example.demo.api.f1;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddRacerToResult {
    private Long seasonId;
    private Long grandPrixId;
    private Long raceTeamId;
}
