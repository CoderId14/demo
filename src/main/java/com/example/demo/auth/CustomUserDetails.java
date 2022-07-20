package com.example.demo.auth;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class CustomUserDetails implements UserDetails {

    private static final Long serialVersionUID = 1L;

    private final Set<GrantedAuthority> authorities;

    private User user;

    public CustomUserDetails(User user){
        this.user =user;
        this.authorities = (Set<GrantedAuthority>) this.getAuthorities();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<String> authorityList = user.getRoles().stream().map(
                Role::getRoleName
        ).collect(Collectors.toList());
        for (String authority :
                authorityList) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }

    public Long getId(){
        return user.getId();
    }
}
