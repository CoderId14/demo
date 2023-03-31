package com.example.demo.Repository.role;

import com.example.demo.entity.Role;
import com.example.demo.entity.supports.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(ERole eRole);

}
