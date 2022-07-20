package com.example.demo.api.auth;

import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.Service.impl.UserService;
import com.example.demo.auth.JwtManager;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.response.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    private final JwtManager jwtManager;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid SignUpDto signUpRequest) {
        return ResponseEntity.ok(userService.signUp(signUpRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        JwtAuthenticationResponse result = userService.login(loginDto);
        return ResponseEntity.ok(result);

    }


}
