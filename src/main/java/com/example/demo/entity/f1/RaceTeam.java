package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_race_team")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RaceTeam extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;
    @Column(name = "powerUnit")
    private float powerUnit;

    @Column(name = "description", columnDefinition = "VARCHAR(255) CHARACTER SET utf8")
    private String description;

    @ManyToMany(mappedBy = "raceTeams", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Season> seasons;
    @OneToMany(mappedBy = "raceTeam", cascade = CascadeType.ALL)
    List<RacerOfRaceTeam> racerOfRaceTeams;
//    @ManyToMany(mappedBy = "raceTeams", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Racer> racers;



//    @ManyToMany(mappedBy = "racers", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Result> results;
}
