package com.example.demo.api.user;


import com.example.demo.Service.CustomUserDetailsService;

import com.example.demo.Service.impl.UserService;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ChangePasswordResponse;
import com.example.demo.dto.response.ForgotPasswordResponse;

import com.example.demo.dto.response.ObjectResponse;
import com.example.demo.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getEmailByUsername(@RequestParam("usernameOrEmail") String usernameOrEmail) {
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "get email",
                        userService.getEmailbyUsername(usernameOrEmail)));
    }


    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "get email",
                        user.getUser().getEmail()));
    }


    @PostMapping(value = "/addUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody @Valid SignUpDto signUpDto) {
        UserDto userDto = userService.addUser(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    //    2
    @GetMapping("/forgot-password")
    public ResponseEntity<?> formForgotPassword(@RequestParam("token") String token) {
        log.info("Controller: get user from token");
        UserTokenResponse userTokenResponse = userService.getUserFromToken(token);
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "Get user successfully",
                        userTokenResponse));
    }

    //    3
    @PostMapping("/change-password")
    public ResponseEntity<?> updatePasswordByToken(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        log.info("Controller :change-password");
        ChangePasswordResponse changePasswordResponse = userService.updatePasswordByToken(changePasswordDto);
        return ResponseEntity.ok(new ObjectResponse(HttpStatus.CREATED,
                "Change password successfully",
                        changePasswordResponse)
                );
    }

    //    1
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDto request) {
        log.info("forgot password controller");

        return ResponseEntity.ok(new ObjectResponse(HttpStatus.CREATED,
                "Send email successfully",
                userService.forgotPassword(request))
        );

    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        log.info("Delete user id= " + id);
        userService.delete(id);
        return ResponseEntity.ok("Delete user id = " + id);
    }


}
