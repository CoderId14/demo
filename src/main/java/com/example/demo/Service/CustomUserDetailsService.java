package com.example.demo.Service;

import com.example.demo.entity.User;
import com.example.demo.Repository.UserRepo;
import com.example.demo.auth.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;


    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("Username not found: " + username );
        }
        return new CustomUserDetails(user.get());
    }
}
