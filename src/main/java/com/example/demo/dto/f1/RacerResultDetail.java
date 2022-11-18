package com.example.demo.dto.f1;


import lombok.Builder;
import lombok.Data;

import java.util.Date;


public interface RacerResultDetail {
    long getRacerId();
    String getRacerName();
    String getGrandPrixName();
    int getPoint();
    Date getTime();
    Date getFinishTime();
    int getRanking();
}
