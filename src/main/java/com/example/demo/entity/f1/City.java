package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_city")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class City extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(nullable = false,
            name = "nation")
    private Nation nation;
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    List<RaceCourse> raceCourses;
}
