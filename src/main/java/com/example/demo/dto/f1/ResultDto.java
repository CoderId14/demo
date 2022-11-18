package com.example.demo.dto.f1;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Builder
@Data
public class ResultDto {

    private long id;

    private String racerName;

    private String raceTeam;

    private int point;

    private int ranking;

    private int status;

    private LocalDateTime finishTime;

    private String season;
}
