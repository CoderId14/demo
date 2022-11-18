package com.example.demo.dto.f1;

import java.util.Date;

public interface IRacerDto {
     Long getId();
     String getName();
     Date getDateOfBirth();
     String getBio();
     String getNational();
     Long getRacerOfTeamId();
}
