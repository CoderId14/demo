package com.example.demo.api.user;


import com.example.demo.Service.impl.UserService;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.request.ChangePasswordDto;
import com.example.demo.dto.request.ForgotPasswordDto;
import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.dto.response.ChangePasswordResponse;
import com.example.demo.dto.response.ForgotPasswordResponse;

import com.example.demo.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/addUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody @Valid SignUpDto signUpDto){
        UserDto userDto = userService.addUser(signUpDto);
        return ResponseEntity.ok(userDto);
    }
//    2
    @GetMapping("/forgot-password")
    public UserTokenResponse formForgotPassword(@RequestParam("token") String token){
        log.info("Controller: get user from token");
        UserTokenResponse userTokenResponse = userService.getUserFromToken(token);
        return userTokenResponse;
    }
//    3
    @PostMapping("/change-password")
    public ResponseEntity<?> updatePasswordByToken(@RequestBody @Valid ChangePasswordDto changePasswordDto){
        ChangePasswordResponse changePasswordResponse = userService.updatePasswordByToken(changePasswordDto);
        return ResponseEntity.ok(changePasswordResponse);
    }
//    1
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
