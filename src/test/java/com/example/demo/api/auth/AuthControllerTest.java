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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @MockBean
    UserService userService;
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(USER_1_SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void login() throws Exception {
//        when(userService.login(USER_1_LOGIN)).thenReturn(JwtAuthenticationResponse.builder()
//                .accessToken("test")
//                        .username("user1")
//                .build());
//        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
//        param.add("username", USER_1_LOGIN.getUsername());
//        param.add("password", USER_1_LOGIN.getPassword());
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
    void confirm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/signUp/confirm")
                        .param("token", "testtoken")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}