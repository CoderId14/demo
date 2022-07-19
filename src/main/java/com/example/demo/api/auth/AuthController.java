package com.example.demo.api.auth;

import com.example.demo.DTO.LoginDto;
import com.example.demo.DTO.SignUpDto;
import com.example.demo.Service.impl.UserService;
import com.example.demo.api.payload.JwtAuthenticationResponse;
import com.example.demo.auth.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    private final JwtManager jwtManager;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpDto> signUp(@RequestBody SignUpDto signUpRequest) {
        return ResponseEntity.ok(userService.signUp(signUpRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody  LoginDto loginDto) {
        LoginDto result = userService.login(loginDto);
        return ResponseEntity.ok(result);

    }


}
