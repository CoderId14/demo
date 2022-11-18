package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_result")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Result extends BaseEntity {

    @Column
    private int point;
    @Column
    private int ranking;
    @Column
    private int status;
    @Column
    private LocalDateTime startTime;
    @Column
    private LocalDateTime finishTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,
            name = "racer_of_team_id")
    private RacerOfRaceTeam racerOfRaceTeam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,
            name = "grand_prix_id")
    private GrandPrix grandPrix;
//    @ManyToMany(fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    @JoinTable(name = "tbl_result_racer", joinColumns = {
//            @JoinColumn(name = "resultId")},
//            inverseJoinColumns = {
//                    @JoinColumn(name = "racerId")})
//    private Set<Racer> racers;
//    @ManyToMany(fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    @JoinTable(name = "tbl_result_raceTeam", joinColumns = {
//            @JoinColumn(name = "resultId")},
//            inverseJoinColumns = {
//                    @JoinColumn(name = "raceTeamId")})
//    private List<RaceTeam> raceTeams;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    @JoinTable(name = "tbl_result_grandPrix", joinColumns = {
//            @JoinColumn(name = "resultId")},
//            inverseJoinColumns = {
//                    @JoinColumn(name = "grandPrixId")})
//    private List<GrandPrix> grandPrixes;
}
