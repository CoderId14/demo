package com.example.demo.api.auth;

import com.example.demo.Service.user.UserService;
import com.example.demo.api.auth.request.*;
import com.example.demo.api.auth.request.SignUpRequest;
import com.example.demo.api.user.response.UserResponse;
import com.example.demo.api.auth.response.JwtAuthenticationResponse;
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


    @PostMapping("/v1/register")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        log.info("Signup controller");
        UserResponse user = userService.signUp(signUpRequest);
        URI uri = URI.create("/api/auth/register");
        return ResponseEntity.created(uri).body(new ObjectResponse(HttpStatus.CREATED,
                "Register successfully",
                user));
    }
    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        log.info("Login controller");
        JwtAuthenticationResponse result = userService.login(loginRequest);
        return ResponseEntity.ok(new ObjectResponse(HttpStatus.OK, "Login successfully", result));

    }

    @PostMapping("/v1/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(userService.refreshToken(request));
    }
    @GetMapping("/v1/register/confirm")
    public String confirm(@RequestParam String token){
        return userService.confirmToken(token);
    }


    @PostMapping("/v1/register/username")
    public boolean isUsernameExist(@RequestBody @Valid CheckUsernameRequest checkUsernameRequest){
        return userService.isUsernameExist(checkUsernameRequest);
    }
    @PostMapping("/v1/register/email")
    public boolean isEmailExist(@RequestBody @Valid CheckEmailRequest checkEmailRequest){
        return userService.isEmailExist(checkEmailRequest);
    }


}
