package com.example.demo.api.user;


import com.example.demo.Service.book.BookLikeService;
import com.example.demo.Service.book.BookService;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.Service.user.UserHistoryService;
import com.example.demo.Service.user.UserService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.auth.request.SignUpRequest;
import com.example.demo.api.user.request.ChangePasswordRequest;
import com.example.demo.api.user.request.ForgotPasswordDto;
import com.example.demo.api.user.request.LikeBookRequest;
import com.example.demo.api.user.request.UserBookHistoryRequest;
import com.example.demo.api.user.response.ChangePasswordResponse;
import com.example.demo.api.user.response.UserResponse;
import com.example.demo.api.user.response.UserTokenResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.config.CacheConfig;
import com.example.demo.config.VnpayConfig;
import com.example.demo.dto.response.ObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final UserHistoryService userHistoryService;
    private final BookService bookService;
    private final CacheConfig cacheConfig;

    private final RoleUtils roleUtils;

    private final VnpayConfig vnpayConfig;

    private final BookLikeService bookLikeService;

    @GetMapping("/v1")
    public ResponseEntity<?> getEmailByUsername(@RequestParam("usernameOrEmail") String usernameOrEmail) {
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "get email",
                        userService.getEmailbyUsername(usernameOrEmail)));
    }

    @GetMapping("/v1/reading-history")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getReadingHistory(
            @RequestParam(value = "userId", required = false) Long userId,
            @PageableDefault(sort = "createdDate",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable,
            @CurrentUser CustomUserDetails user) {
        roleUtils.checkAuthorization(user.getUsername(), user);
        UserBookHistoryRequest request;
        if(userId != null){
             request = UserBookHistoryRequest.builder()
                    .userId(userId)
                    .page(pageable.getPageNumber())
                    .size(pageable.getPageSize())
                    .build();
        } else{
            request = UserBookHistoryRequest.builder()
                    .userId(user.getId())
                    .page(pageable.getPageNumber())
                    .size(pageable.getPageSize())
                    .build();
        }

        return ResponseEntity.ok(userHistoryService.getHistory(request, user));
    }

    @GetMapping("/v1/current-user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(
                new ObjectResponse(HttpStatus.OK,
                        "get email",
                        user.getUser().getEmail()));
    }

    @GetMapping("/v1/user-info")
    public ResponseEntity<?> getUserInfo(@CurrentUser CustomUserDetails currentUser,
                                         @RequestParam(value = "userId", required = false) Long userId) {
        return ResponseEntity.ok().body(userService.getUserInfo(currentUser, userId));
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
    public ResponseEntity<?> getBookLike(@RequestParam int page, @RequestParam int size, @RequestParam long userId){
        log.info("get book liked by  user id= " + userId);
        return ResponseEntity.ok(bookService.findBookLikeByUserId(userId,PageRequest.of(page,size,Sort.by("createdBy"))));
    }
    @PostMapping("/v1/like-book")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> likeBook(@RequestBody @Validated LikeBookRequest request, @CurrentUser CustomUserDetails currentUser){
        long userId = currentUser.getId();
        bookLikeService.liked(userId, request.getBookId());
         return ResponseEntity.ok("Liked success book = " + request.getBookId());
    }

    @PostMapping("/v1/unLike-book")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> unLikeBook(@RequestBody @Validated LikeBookRequest request, @CurrentUser CustomUserDetails currentUser){
        long userId = currentUser.getId();
        bookLikeService.unLiked(userId, request.getBookId());
        return ResponseEntity.ok("unLike success book = " + request.getBookId());
    }

    @GetMapping("/v1/load-coin")
    public ResponseEntity<?> loadCoin(@RequestParam(value = "userId") Long userId, @RequestParam(value = "coin") Long coin)
        throws Exception {
        String vnp_Version = vnpayConfig.vnp_Version;
        String vnp_Command = vnpayConfig.vnp_Command;
        String orderType = vnpayConfig.orderType;
        long amount = coin*1000*100;
        // mã thanh toán gửi sang là duy nhất nên cần set session (UUID), lưu cái session nayf lại, nhưng lấy ra để lấy userid kiểu gì
        String vnp_TxnRef = VnpayConfig.getRandomNumber(userId);
        String vnp_IpAddr = vnpayConfig.vnp_IpAddr;
        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;
        cacheConfig.put(vnp_TxnRef, userId);
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán hóa đơn thuế:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        cld.add(Calendar.HOUR, 7);
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(vnpayConfig.vnp_HashSecret, hashData.toString());
        System.out.println("hashdata is: "+ hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpayConfig.vnp_PayUrl + "?" + queryUrl;
//        System.out.println(paymentUrl);
        return ResponseEntity.ok(paymentUrl);
    }
    @GetMapping("/v1/save-coin")
    public ResponseEntity<?> saveCoin(HttpServletRequest request) throws Exception {
        Map<String, String[]> params = request.getParameterMap();
        HashMap<String, String> paramMap = new HashMap<>();
        Set<String> keySets = params.keySet();
        for (String key : keySets) {
            String[] values = params.get(key);
            paramMap.put(key, values[0]);
        }
        String vnp_SecureHash = paramMap.get("vnp_SecureHash");
        paramMap.remove("vnp_SecureHash");
        List fieldNames = new ArrayList(paramMap.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) paramMap.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        if(VnpayConfig.hmacSHA512(vnpayConfig.vnp_HashSecret,hashData.toString()).equals(vnp_SecureHash)==false){
            return ResponseEntity.ok("70");
        }
        Long userId = cacheConfig.get(paramMap.get("vnp_TxnRef"));
        Long coin = Long.valueOf(paramMap.get("vnp_Amount"))/(100*1000);
        userService.loadCoin(userId,coin);
        cacheConfig.remove(paramMap.get("vnp_TxnRef"));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping("/v1/open-premium")
    public ResponseEntity<?> openPremium(@RequestParam long userId){
        userService.openPremium(userId);
        return ResponseEntity.ok("Open success premium");
    }

}
