package com.example.demo.Service;

import com.example.demo.DTO.LoginDto;
import com.example.demo.DTO.SignUpDto;

public interface IUserService {
    SignUpDto signUp(SignUpDto signUpDto);
    LoginDto login(LoginDto loginDto);

}
