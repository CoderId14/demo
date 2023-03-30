package com.example.demo.api.user;


import com.example.demo.Service.user.UserService;
import com.example.demo.api.user.request.ChangePasswordRequest;
import com.example.demo.api.user.request.ForgotPasswordDto;
import com.example.demo.api.auth.request.SignUpRequest;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.response.ChangePasswordResponse;

import com.example.demo.dto.response.ObjectResponse;
import com.example.demo.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
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


    @PostMapping(value = "/v1/addUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        UserDto userDto = userService.addUser(signUpRequest);
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
    public ResponseEntity<?> updatePasswordByToken(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        log.info("Controller :change-password");
        ChangePasswordResponse changePasswordResponse = userService.updatePasswordByToken(changePasswordRequest);
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
