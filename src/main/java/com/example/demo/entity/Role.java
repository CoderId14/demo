package com.example.demo.entity;



import com.example.demo.entity.supports.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@PersistenceContext
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", unique = true, nullable = false)
    private ERole roleName;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> users;
}