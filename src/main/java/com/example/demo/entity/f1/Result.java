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
    private LocalDateTime finishTime;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(nullable = false,
            name = "racerOfTeam")
    private RacerOfRaceTeam racerOfRaceTeam;
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(nullable = false,
            name = "grandPrix")
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
