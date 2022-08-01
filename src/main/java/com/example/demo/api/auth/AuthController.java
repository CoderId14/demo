package com.example.demo.api.auth;

import com.example.demo.Service.CustomUserDetailsService;
import com.example.demo.Service.impl.ConfirmationTokenService;
import com.example.demo.Service.impl.UserService;
import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.request.SignUpDto;

import com.example.demo.auth.JwtManager;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.response.JwtAuthenticationResponse;
import com.example.demo.dto.response.ObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class AuthController {
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto signUpRequest) {
        log.info("Signup controller");
        UserDto user = userService.signUp(signUpRequest);
        URI uri = URI.create("{baseUrl} + /api/auth/register");
        return ResponseEntity.created(uri).body(new ObjectResponse(HttpStatus.CREATED,
                "Register user: " + user.getName(),
                user));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        log.info("Login controller");
        JwtAuthenticationResponse result = userService.login(loginDto);
        return ResponseEntity.ok(new ObjectResponse(HttpStatus.OK, "Login successfully", result));

    }

    @GetMapping("/signUp/confirm")
    public String confirm(@RequestParam("token") String token){
        return userService.confirmToken(token);
    }



}
