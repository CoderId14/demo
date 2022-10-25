package com.example.demo.api.auth;

import com.example.demo.Service.impl.UserService;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.request.*;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class AuthController {
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto signUpRequest) {
        log.info("Signup controller");
        UserDto user = userService.signUp(signUpRequest);
        URI uri = URI.create("/api/auth/register");
        return ResponseEntity.created(uri).body(new ObjectResponse(HttpStatus.CREATED,
                "Register successfully",
                user));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        log.info("Login controller");
        JwtAuthenticationResponse result = userService.login(loginDto);
        return ResponseEntity.ok(new ObjectResponse(HttpStatus.OK, "Login successfully", result));

    }

    @PostMapping("/register/confirm")
    public String confirm(@RequestBody ConfirmationDto token){
        return userService.confirmToken(token.getToken());
    }


    @PostMapping("/register/username")
    public boolean isUsernameExist(@RequestBody @Valid CheckUsername checkUsername){
        return userService.isUsernameExist(checkUsername);
    }
    @PostMapping("/register/email")
    public boolean isEmailExist(@RequestBody @Valid CheckEmail checkEmail){
        return userService.isEmailExist(checkEmail);
    }


}
