package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_season")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Season extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    List<GrandPrix> grandPrixes;

    @ManyToMany(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JoinTable(name = "tbl_season_raceteam", joinColumns = {
            @JoinColumn(name = "seasonId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "raceTeamId")})
    private Set<Racer> raceTeams;
}