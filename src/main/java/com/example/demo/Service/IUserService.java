package com.example.demo.Service;

import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.response.JwtAuthenticationResponse;

public interface IUserService {
    UserDto signUp(SignUpDto signUpDto);
    JwtAuthenticationResponse login(LoginDto loginDto);

}
