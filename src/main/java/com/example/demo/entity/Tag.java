package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="tbl_tag",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
}
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tag extends BaseEntity{

    @Column
    private String name;
    @Column
    private String description;

    //thêm JsonIgnore vì This is an issue with bidirectional relationships,
    // as they hold references to each other, at deserialization, Jackson runs in an infinite loop
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Book> books = new HashSet<>();
}
