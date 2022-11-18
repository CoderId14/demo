package com.example.demo.dto.f1;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;


public interface ResultRacerDto {
     long getRacerId();
     int getRanking();
     String getRacerName();
     String getRaceTeamName();
     String getNational();
     int getTotalPoints();
     Date getTotalTimes();
}
