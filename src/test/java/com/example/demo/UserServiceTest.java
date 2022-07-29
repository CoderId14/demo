package com.example.demo;


import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.impl.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;

import java.util.stream.IntStream;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepo userRepo;

    @Autowired
    private UserService userService;

    @BeforeAll
    public void setUp(){

    }
}
