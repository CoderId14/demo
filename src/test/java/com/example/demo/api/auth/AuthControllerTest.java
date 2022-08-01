package com.example.demo.api.auth;

import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.impl.UserService;
import com.example.demo.auth.JwtSecurityFilter;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.dto.response.JwtAuthenticationResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    UserRepo userRepo;
    @MockBean
    JwtSecurityFilter jwtSecurityFilter;
    @Autowired
    MockMvc mockMvc;

    LoginDto USER_1_LOGIN;
    SignUpDto USER_1_SIGNUP;

    User user;
    @BeforeEach
    void init(){
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
        USER_1_LOGIN = LoginDto.builder()
                .username("user1")
                .password("user1")
                .build();
        USER_1_SIGNUP = SignUpDto.builder()
                .username("user1")
                .password("user1")
                .email("user1@gmail.com")
                .build();
    }

    @Test
    void signUp() throws Exception {
        UserDto user = UserDto.builder()
                .username(USER_1_SIGNUP.getUsername())
                .email(USER_1_SIGNUP.getEmail())
                .isActive(true)
                .build();
//        when(userService.signUp(USER_1_SIGNUP)).thenReturn(user);
//        mockMvc.perform(post("/api/auth/register", USER_1_SIGNUP))
//                .andExpect(status().isCreated());
    }

    @Test
    void login() throws Exception {
//        when(userService.login(USER_1_LOGIN)).thenReturn(JwtAuthenticationResponse.builder()
//                .accessToken("test")
//                        .username("user1")
//                .build());
        when(userRepo.findByUsername("user1")).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(USER_1_LOGIN.getPassword(),"user1")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(asJsonString(USER_1_LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void confirm() {
    }
}