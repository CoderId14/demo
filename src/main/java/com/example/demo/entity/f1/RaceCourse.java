package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_race_course")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RaceCourse extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;

    @ManyToOne()
    @JoinColumn(nullable = false,
            name = "city_id")
    private City city;

    @OneToMany(mappedBy = "raceCourse")
    Set<GrandPrix> grandPrixes;

}