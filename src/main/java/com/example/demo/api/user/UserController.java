package com.example.demo.api.user;


import com.example.demo.Service.book.BookService;
import com.example.demo.Service.user.UserHistoryService;
import com.example.demo.Service.user.UserService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.auth.request.SignUpRequest;
import com.example.demo.api.user.request.ChangePasswordRequest;
import com.example.demo.api.user.request.ForgotPasswordDto;
import com.example.demo.api.user.request.UserBookHistoryRequest;
import com.example.demo.api.user.response.ChangePasswordResponse;
import com.example.demo.api.user.response.UserResponse;
import com.example.demo.api.user.response.UserTokenResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.response.ObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final UserHistoryService userHistoryService;
    private final BookService bookService;

    @GetMapping("/v1")
    public ResponseEntity<?> getEmailByUsername(@RequestParam("usernameOrEmail") String usernameOrEmail) {
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "get email",
                        userService.getEmailbyUsername(usernameOrEmail)));
    }

    @GetMapping("/v1/reading-history")
    public ResponseEntity<?> getReadingHistory(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @CurrentUser CustomUserDetails user) {

        UserBookHistoryRequest request = UserBookHistoryRequest.builder()
                .userId(userId)
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(userHistoryService.getHistory(request, user));
    }

    @GetMapping("/v1/current-user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "get email",
                        user.getUser().getEmail()));
    }


    @PostMapping(value = "/v1/addUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        UserResponse userResponse = userService.addUser(signUpRequest);
        return ResponseEntity.ok(userResponse);
    }

    //    2
    @GetMapping("/v1/forgot-password")
    public ResponseEntity<?> formForgotPassword(@RequestParam("token") String token) {
        log.info("Controller: get user from token");
        UserTokenResponse userTokenResponse = userService.getUserFromToken(token);
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "Get user successfully",
                        userTokenResponse));
    }

    //    3
    @PostMapping("/v1/change-password")
    public ResponseEntity<?> updatePasswordByToken(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        log.info("Controller :change-password");
        ChangePasswordResponse changePasswordResponse = userService.updatePasswordByToken(changePasswordRequest);
        return ResponseEntity.ok(new ObjectResponse(HttpStatus.CREATED,
                "Change password successfully",
                changePasswordResponse)
        );
    }

    //    1
    @PostMapping("/v1/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDto request) {
        log.info("forgot password controller");

        return ResponseEntity.ok(new ObjectResponse(HttpStatus.CREATED,
                "Send email successfully",
                userService.forgotPassword(request))
        );

    }

    @DeleteMapping("/v1")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        log.info("Delete user id= " + id);
        userService.delete(id);
        return ResponseEntity.ok("Delete user id = " + id);
    }

    @GetMapping("/v1/get-books-liked")
    public ResponseEntity<?> getBookLike(@RequestParam int page, @RequestParam int size, @RequestParam long userid){
        log.info("get book liked by  user id= " + userid);
        return ResponseEntity.ok(bookService.findBookLikeByUserId(userid,PageRequest.of(page,size,Sort.by("createdBy"))));
    }
    @PostMapping("/v1/like-book")
    public ResponseEntity<?> likeBook(@RequestParam long userid, @RequestParam long bookid){
         bookService.liked(userid,bookid);
         return ResponseEntity.ok("Liked success book = " + bookid);
    }

}
