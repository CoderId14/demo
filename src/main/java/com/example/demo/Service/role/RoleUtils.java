package com.example.demo.Service.role;

import com.example.demo.Repository.UserRoleRepo;
import com.example.demo.Repository.role.RoleRepo;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.entity.user.UserRole;
import com.example.demo.exceptions.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.demo.entity.supports.ERole.ROLE_ADMIN;
import static com.example.demo.entity.supports.ERole.ROLE_USER_VIP;
import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Transactional
@Service
public class RoleUtils {
    private final RoleRepo roleRepo;

    private final UserRoleRepo userRoleRepo;

    public void checkAuthorization(String username, CustomUserDetails currentUser) {
        if (username.equals(currentUser.getUsername()) || currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepo.findRoleByRoleName(ROLE_ADMIN).toString()))) {
            return;
        }
        throw new UnauthorizedException("You don't have enough permission");
    }

    public void checkPremium(boolean bookPremium, CustomUserDetails currentUser) {
        if (!bookPremium) {
            return;
        }
        if (currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepo.findRoleByRoleName(ROLE_ADMIN).toString()))) {
            return;
        }
        Optional<UserRole> userRole = userRoleRepo.findByRole_RoleNameAndUser(ROLE_USER_VIP, currentUser.getUser());
        if (userRole.isPresent()) {

            if (userRole.get().getValidUntil().isBefore(now())) {
                throw new UnauthorizedException("You must purchase Premium to access");
            }
            return;
        }
        throw new UnauthorizedException("You don't have enough permission");
    }


}
