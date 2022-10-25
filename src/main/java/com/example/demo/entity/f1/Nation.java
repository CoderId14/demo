package com.example.demo.entity.f1;

import com.example.demo.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_nation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Nation extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;

    @OneToMany(mappedBy = "national", cascade = CascadeType.ALL)
    List<Racer> racers;

    @OneToMany(mappedBy = "nation", cascade = CascadeType.ALL)
    List<City> cities;


}
