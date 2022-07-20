package com.example.demo.Service.impl;

import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.RoleRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.CustomUserDetailsService;
import com.example.demo.Service.IUserService;
import com.example.demo.auth.CustomUserDetails;
import com.example.demo.auth.JwtManager;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.response.JwtAuthenticationResponse;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.Utils.Constant.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final JwtManager jwtManager;

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public UserDto signUp(SignUpDto signUpDto) {
        log.info("Sign up Service");
        Set<Role> roles = new HashSet<>();
        if(userRepo.existsByUsername(signUpDto.getUsername())){
            throw new ResourceNotFoundException("User", "Username", signUpDto.getUsername());
        }
        if(userRepo.existsByEmail(signUpDto.getEmail())){
            throw new ResourceNotFoundException("User", "Email", signUpDto.getEmail());
        }
        Set<Role> role = new HashSet<>();
        role.add(
                roleRepo.findRoleByRoleName(ROLE_USER).orElseThrow(
                        () -> new ResourceNotFoundException("Role", "Role name", ROLE_USER)
                ));
        User user = User.builder()
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .email(signUpDto.getEmail())
                .isActive(true)
                .roles(role)
                .build();
        userRepo.save(user);
        return Mapper.toUserDto(user);

    }

    @Override
    public JwtAuthenticationResponse login(LoginDto loginDto) {
        log.info("Login service");
        CustomUserDetails userDetails;
        try {
            userDetails = customUserDetailsService.loadUserByUsername(loginDto.getUsername());

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        if (passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            log.info("password matched");

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", userDetails.getUsername());

            String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            claims.put("roles", authorities);
            claims.put("userId", userDetails.getId());
            String subject = userDetails.getUsername();
            String jwt = jwtManager.generateToken(claims, subject);
            return Mapper.toJwtAuthenticationRepsonse(jwt, userDetails);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
    }
}
