package com.example.demo.entity.user;


import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_user_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@PersistenceContext
public class UserRole extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;
}