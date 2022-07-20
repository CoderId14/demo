package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity {


    @Column(name = "Username", length = 30, nullable = false, unique = true)
    private String username;

    @Column(name = "Password", length = 100, nullable = false)
    private String password;

    @Column(name = "Email", length = 40, nullable = false, unique = true)
    private String email;

    @Column(name = "Name", columnDefinition = "VARCHAR(50) CHARACTER SET utf8")
    private String name;

    @Lob
    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "Verification_code", updatable = false)
    private String verificationCode;

    @Column(name = "Reset_password_code")
    private String resetPasswordCode;


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
