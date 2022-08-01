package com.example.demo.Service;

import com.example.demo.Repository.UserRepo;
import com.example.demo.auth.JwtManager;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.response.JwtAuthenticationResponse;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.demo.Utils.Vadilate.isEmail;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService, Serializable {
    private final UserRepo userRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user;
        if(isEmail(usernameOrEmail)){
            user = userRepo.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Email", usernameOrEmail)
            );
        }
        else{
            user = userRepo.findByUsername(usernameOrEmail).orElseThrow(
                    ()-> new ResourceNotFoundException("User", "Username", usernameOrEmail)
            );
        }
        return new CustomUserDetails(user);
    }
}
