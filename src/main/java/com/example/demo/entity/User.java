package com.example.demo.entity;

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


    @Column(name = "username", length = 30, unique = true)
    private String username;

    @Column(name = "password", length = 100, nullable = true)
    private String password;

    @Column(name = "email", length = 40, nullable = false, unique = true)
    private String email;

    @Column(name = "Name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<ConfirmationToken> confirmationToken;
    @Lob
    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Column(name ="provider")
    private AuthProvider provider;
    @Column(name = "providerId")
    private String providerId;



    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JoinTable(name = "tbl_user_role", joinColumns = {
            @JoinColumn(name = "UserId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "RoleId")})
    private Set<Role> roles;
}
