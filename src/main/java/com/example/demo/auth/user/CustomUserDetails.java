package com.example.demo.auth.user;

import com.example.demo.entity.Role;
import com.example.demo.entity.supports.ERole;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Data
public class CustomUserDetails implements UserDetails, OAuth2User {

    private static final Long serialVersionUID = 1L;

    private final Set<GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    private User user;

    public CustomUserDetails(User user){
        this.user =user;
        this.authorities = (Set<GrantedAuthority>) this.getAuthorities();
    }

    public CustomUserDetails (User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
        this.authorities = (Set<GrantedAuthority>) this.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<ERole> authorityList = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .toList();
        for (ERole authority :
                authorityList) {
            authorities.add(new SimpleGrantedAuthority(authority.toString()));
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

    @Override
    public String getName() {
        return null;
    }
}
