package com.example.demo.Service.user;

import com.example.demo.Repository.user.UserRepo;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.entity.user.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static com.example.demo.Utils.AppUtils.isEmail;


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
