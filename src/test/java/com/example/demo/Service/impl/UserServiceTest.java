package com.example.demo.Service.impl;

import com.example.demo.Repository.RoleRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.CustomUserDetailsService;
import com.example.demo.auth.JwtManager;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exceptions.user.ResourceExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static com.example.demo.Utils.Constant.ROLE_USER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepo userRepo;
    @Mock
    RoleRepo roleRepo;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtManager jwtManager;


    @Mock
    CustomUserDetailsService customUserDetailsService;
    LoginDto USER_1_LOGIN;
    SignUpDto USER_1_SIGNUP;
    User user;
    @BeforeEach
    void setUp(){
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder()
            .roleName("ROLE_USER")
            .build());
         user =User.builder()
                .roles(roles)
                .username("user1")
                .password("user1")
                .isActive(true)
                .email("user1@gmail.com")
                .build();


        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        USER_1_LOGIN = LoginDto.builder()
                .username("user1")
                .password("user1")
                .build();
        USER_1_SIGNUP = SignUpDto.builder()
                .username("user1")
                .password("user1")
                .email("user1@gmail.com")
                .build();
//
//        USER_1_SIGNUP = SignUpDto.builder()
//                .username("user1")
//                .password("user1")
//                .email("user1@gmail.com")
//                .build();


    }

    @Test
    @DisplayName("LOGIN SUCCESS WITH USERNAME")
    void login() {
        when(customUserDetailsService.loadUserByUsername(USER_1_LOGIN.getUsername())).thenReturn(new CustomUserDetails(user));
        when(passwordEncoder.matches(USER_1_LOGIN.getPassword(),"user1")).thenReturn(true);
        String test = userService.login(USER_1_LOGIN).getUsername();
        Assertions.assertEquals("user1", test);
    }
    @Test
    @DisplayName("SIGN UP SUCCESS WHEN USERNAME AND EMAIL IS UNIQUE")
    void signUp(){
//        when(userRepo.save(user)).thenReturn(user);
//        when(userRepo.existsByUsername("user1")).thenReturn(false);
//        when(userRepo.existsByEmail("user1@gmail.com")).thenReturn(false);
        UserDto user_test = userService.addUser(USER_1_SIGNUP);
        String test = user_test.getUsername();
        Assertions.assertEquals("user1", test);
    }
    @Test
    @DisplayName("SIGN UP WITH EXIST USERNAME")
    void signUpWithExistUsername(){
            when(userRepo.existsByUsername("user1")).thenReturn(true);
            Assertions.assertThrows(ResourceExistsException.class,() -> userService.addUser(USER_1_SIGNUP));
    }
    @Test
    @DisplayName("SIGN UP WITH EXIST EMAIL")
    void signUpWithExistEmail(){
        when(userRepo.existsByEmail("user1@gmail.com")).thenReturn(true);
        Assertions.assertThrows(ResourceExistsException.class,() -> userService.addUser(USER_1_SIGNUP));
    }
}