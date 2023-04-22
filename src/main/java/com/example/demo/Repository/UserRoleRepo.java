package com.example.demo.Repository;

import com.example.demo.entity.supports.ERole;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole_RoleNameAndUser(ERole roleName, User user);

}
