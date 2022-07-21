package com.example.demo.api.user;


import com.example.demo.Service.impl.UserService;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.request.ChangePasswordDto;
import com.example.demo.dto.request.ForgotPasswordDto;
import com.example.demo.dto.response.ChangePasswordResponse;
import com.example.demo.dto.response.ForgotPasswordResponse;
import com.example.demo.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/forgot-password")
    public TokenResponse formForgotPassword(@RequestParam("token") String token){
        log.info("Controller: get user from token");
        TokenResponse tokenResponse = userService.getUserFromToken(token);
        return tokenResponse;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> updatePasswordByToken(@RequestBody @Valid ChangePasswordDto changePasswordDto){
        ChangePasswordResponse changePasswordResponse = userService.updatePasswordByToken(changePasswordDto);
        return ResponseEntity.ok(changePasswordResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDto request){
        log.info("forgot password controller");

        return ResponseEntity.ok(userService.forgotPassword(request));

    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id){
        log.info("Delete user id= "+id);
        userService.delete(id);
        return ResponseEntity.ok("Delete user id = " +id);
    }


}
