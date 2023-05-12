package com.example.demo.entity.user;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.ConfirmationToken;
import com.example.demo.entity.Role;
import com.example.demo.entity.supports.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity {


    @Column(length = 30, unique = true)
    private String username;

    @Column(length = 100)
    private String password;

    @Column(length = 40, nullable = false, unique = true)
    private String email;

    @Column()
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<ConfirmationToken> confirmationToken;
    @Lob
    @Column
    private String avatar;

    @Column(nullable = false)
    private Boolean isActive;

    @Column
    private AuthProvider provider;
    @Column
    private String providerId;
    @Column
    @Builder.Default
    private Long coin = 0L;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<UserRole> userRoles;
}
