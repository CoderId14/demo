package com.example.demo.Service;

import com.example.demo.Service.impl.UserService;
import com.example.demo.entity.User;
import com.example.demo.Repository.UserRepo;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

import static com.example.demo.Utils.Vadilate.isEmail;


@Service

@RequiredArgsConstructor
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
