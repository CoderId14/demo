package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbl_racer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Racer extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;
    @Column(name = "dateOfBirth")

    private Date dateOfBirth;

    @Column(name = "bio", columnDefinition = "VARCHAR(255) CHARACTER SET utf8")
    private String bio;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    @JoinTable(name = "tbl_racer_raceteam", joinColumns = {
//            @JoinColumn(name = "racerId")},
//            inverseJoinColumns = {
//                    @JoinColumn(name = "raceTeamId")})
//    private Set<RaceTeam> raceTeams;



    @ManyToOne
    @JoinColumn(name = "national")
    private Nation national;
//    @ManyToMany(mappedBy = "raceTeams", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Result> results;
}
