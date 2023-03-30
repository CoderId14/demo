package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name ="tbl_category")
@Getter
@Setter
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity{
    @Column
    private String name;
    @Column
    private String description;
    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Book> books;
}
