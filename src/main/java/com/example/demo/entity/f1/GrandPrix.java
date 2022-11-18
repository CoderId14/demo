package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tbl_grandPrix")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GrandPrix extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;
    @Column(name = "laps", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private int laps;
    @Column(name = "time")
    private LocalDateTime time;
    @Column(name = "description", columnDefinition = "VARCHAR(255) CHARACTER SET utf8")
    private String description;

    @ManyToOne
    @JoinColumn(name = "race_course_id")
    private RaceCourse raceCourse;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    @JoinTable(name = "tbl_racer_raceteam", joinColumns = {
//            @JoinColumn(name = "racerId")},
//            inverseJoinColumns = {
//                    @JoinColumn(name = "raceTeamId")})
//    private Set<RacerOfRaceTeam> racerOfRaceTeams;
    @OneToMany(mappedBy = "grandPrix")
    Set<Result> results;
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(nullable = false,
            name = "season_id")
    private Season season;
//    @ManyToMany(mappedBy = "grandPrixes", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Result> results;
}