package com.example.demo.entity.f1;


import com.example.demo.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_racer_team")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RacerOfRaceTeam extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "racer_id")
    Racer racer;

    @ManyToOne
    @JoinColumn(name = "race_team_id")
    RaceTeam raceTeam;

    private boolean status;

    @OneToMany(mappedBy = "racerOfRaceTeam", cascade = CascadeType.ALL)
    List<Result> results;
}
