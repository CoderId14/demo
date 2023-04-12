package com.example.demo.Service.role;

import com.example.demo.Repository.role.RoleRepo;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.exceptions.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.entity.supports.ERole.ROLE_ADMIN;

@RequiredArgsConstructor
@Transactional
@Service
public class RoleUtils {
    private final RoleRepo roleRepo;

    public void checkAuthorization(String username, CustomUserDetails currentUser){
        if(username.equals(currentUser.getUsername()) || currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepo.findRoleByRoleName(ROLE_ADMIN).toString()))){
            return;
        }
        throw new UnauthorizedException("You don't have enough permission");
    }
}
