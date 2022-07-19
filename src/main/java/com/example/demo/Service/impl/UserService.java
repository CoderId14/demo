package com.example.demo.Service.impl;

import com.example.demo.DTO.LoginDto;
import com.example.demo.DTO.SignUpDto;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.RoleRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.CustomUserDetailsService;
import com.example.demo.Service.IUserService;
import com.example.demo.auth.CustomUserDetails;
import com.example.demo.auth.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.Utils.Constant.ROLE_ADMIN;
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
    public SignUpDto signUp(SignUpDto signUpDto) {
        log.info("Sign up Service");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(signUpDto.getPassword());





        User user = User.builder()
                .username(signUpDto.getUsername())
                .password(encodedPassword)
                .email(signUpDto.getEmail())
                .build();
        userRepo.save(user);

        Set<Role> authorities = new HashSet<>();
        authorities.add(Role.builder().user(user).name(ROLE_USER).build());
        authorities.add(Role.builder().user(user).name(ROLE_ADMIN).build());
        roleRepo.saveAll(authorities);

        return SignUpDto.builder()
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .email(signUpDto.getEmail())
                .status("Success")
                .build();
    }

    @Override
    public LoginDto login(LoginDto loginDto) {
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
            return LoginDto.builder().token(jwt).build();
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
    }
}
