package com.example.demo.Service.user;

import com.example.demo.Repository.UserRoleRepo;
import com.example.demo.Repository.role.RoleRepo;
import com.example.demo.Repository.user.UserRepo;
import com.example.demo.Service.auth.RefreshTokenService;
import com.example.demo.Service.confirmation.ConfirmationTokenService;
import com.example.demo.Service.email.IEmailSender;
import com.example.demo.Utils.AppConstants;
import com.example.demo.Utils.MailBuilder;
import com.example.demo.api.auth.request.*;
import com.example.demo.api.auth.response.JwtAuthenticationResponse;
import com.example.demo.api.auth.response.TokenRefreshResponse;
import com.example.demo.api.user.request.ChangePasswordRequest;
import com.example.demo.api.user.request.ForgotPasswordDto;
import com.example.demo.api.user.response.ChangePasswordResponse;
import com.example.demo.api.user.response.ForgotPasswordResponse;
import com.example.demo.api.user.response.UserResponse;
import com.example.demo.api.user.response.UserTokenResponse;
import com.example.demo.auth.JwtManager;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.constant.PremiumConstance;
import com.example.demo.dto.Mapper;
import com.example.demo.entity.ConfirmationToken;
import com.example.demo.entity.Role;
import com.example.demo.entity.auth.RefreshToken;
import com.example.demo.entity.supports.AuthProvider;
import com.example.demo.entity.supports.ERole;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserRole;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.auth.TokenRefreshException;
import com.example.demo.exceptions.auth.UnauthorizedException;
import com.example.demo.exceptions.user.AccountActiveException;
import com.example.demo.exceptions.user.InsufficientEx;
import com.example.demo.exceptions.user.ResourceExistsException;
import com.example.demo.exceptions.user.TokenInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.Utils.AppUtils.isEmail;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final AppConstants appConstant;

    private final JwtManager jwtManager;

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final ConfirmationTokenService confirmationTokenService;

    private final IEmailSender emailService;

    private final MailBuilder mailBuilder;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    private final RefreshTokenService refreshTokenService;

    private final UserRoleRepo userRoleRepo;

    public boolean isUsernameExist(CheckUsernameRequest checkUsernameRequest){
        return userRepo.findByUsername(checkUsernameRequest.getUsername()).isPresent();
    }
    public boolean isEmailExist(CheckEmailRequest checkEmailRequest){
        return userRepo.findByEmail(checkEmailRequest.getEmail()).isPresent();
    }

    public String getEmailbyUsername(String usernameOrEmail) {
        User user;
        if (isEmail(usernameOrEmail)) {
            user = userRepo.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Email", usernameOrEmail)
            );
        } else {
            user = userRepo.findByUsername(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Username", usernameOrEmail)
            );
        }
        return user.getEmail();
    }

    //    Test
    public UserResponse addUser(SignUpRequest signUpRequest) {

        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            throw new ResourceExistsException("User", "Username", signUpRequest.getUsername());
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw new ResourceExistsException("User", "Email", signUpRequest.getEmail());
        }

        Role defaultRole = roleRepo.findRoleByRoleName(ERole.ROLE_USER)
                .orElseGet(() -> roleRepo.save(Role.builder().roleName(ERole.ROLE_USER).build()));

        UserRole userRole = UserRole.builder()
                .role(defaultRole)
                .validUntil(LocalDateTime.now().plusYears(10))
                .build();

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .isActive(true)
                .userRoles(new HashSet<>(Collections.singletonList(userRole)))
                .build();
        userRole.setUser(user);
        userRepo.save(user);
        return Mapper.toUserDto(user);
    }

    public ChangePasswordResponse updatePasswordByToken(ChangePasswordRequest changePasswordRequest) {
        log.info("Service: updatePassword");
        User user;
        ConfirmationToken token = null;
        if (!confirmationTokenService.isTokenValid(changePasswordRequest.getToken())) {
            token = confirmationTokenService.getToken(changePasswordRequest.getToken()).get();
        }

        String usernameOrEmail = changePasswordRequest.getUsernameOrEmail();
        if (isEmail(usernameOrEmail)) {
            user = userRepo.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Email", usernameOrEmail)
            );
        } else {
            user = userRepo.findByUsername(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Username", usernameOrEmail)
            );
        }
        if (token != null && user.getId() == token.getUser().getId()) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
            userRepo.save(user);
            confirmationTokenService.setConfirmDate(changePasswordRequest.getToken());
            return ChangePasswordResponse.builder()
                    .username(usernameOrEmail)
                    .password(changePasswordRequest.getPassword())
                    .build();
        }

        throw new TokenInvalidException("Token invalid");

    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        log.info("forgotPassword Service");
        User user;
        String usernameOrEmail = forgotPasswordDto.getUsernameOrEmail();
        if (isEmail(usernameOrEmail)) {
            user = userRepo.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Email", usernameOrEmail)
            );
        } else {
            user = userRepo.findByUsername(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Username", usernameOrEmail)
            );
        }
        String token = confirmationTokenService.generateConfirmationToken(user);

        String link = AppConstants.LINK_FORGOT_PASSWORD + token;
        emailService.send(user.getEmail(),
                mailBuilder.buildEmailForgotPassword(user.getUsername(), link));
        return new ForgotPasswordResponse(user.getEmail());
    }


    public void processOAuthPostLogin(String email) {
        Optional<User> existUser = userRepo.findByEmail(email);


        if (existUser.isEmpty()) {
            User newUser = User.builder()
                    .email(email)
                    .provider(AuthProvider.google)
                    .isActive(true)
                    .build();
            userRepo.save(newUser);
        }

    }

    @Override
    public UserResponse signUp(SignUpRequest signUpRequest) {
        log.info("Sign up Service");
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            throw new ResourceExistsException("User", "username", signUpRequest.getUsername());
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw new ResourceExistsException("User", "email", signUpRequest.getEmail());
        }
        Role defaultRole = roleRepo.findRoleByRoleName(ERole.ROLE_USER)
                .orElseGet(() -> roleRepo.save(Role.builder().roleName(ERole.ROLE_USER).build()));

        UserRole userRole = UserRole.builder()
                .role(defaultRole)
                .validUntil(LocalDateTime.now().plusYears(10))
                .build();

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .isActive(false)
                .userRoles(new HashSet<>(Collections.singletonList(userRole)))
                .build();

        userRole.setUser(user);

        userRepo.save(user);

        String token = confirmationTokenService.generateConfirmationToken(user);

        String link = AppConstants.LINK_VERIFY + token;
        emailService.send(signUpRequest.getEmail(),
                mailBuilder.buildEmailSignUp(signUpRequest.getUsername(), link));
        return Mapper.toUserDto(user);

    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        log.info("Login service");
        CustomUserDetails userDetails;
        try {
            userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new ResourceNotFoundException("User", "username", loginRequest.getUsername());
        }
        if (!userDetails.getUser().getIsActive()) {
            String token = confirmationTokenService.generateConfirmationToken(userDetails.getUser());

            String link = AppConstants.LINK_VERIFY + token;
            emailService.send(userDetails.getUser().getEmail(),
                    mailBuilder.buildEmailSignUp(userDetails.getUser().getUsername(), link));
            throw new AccountActiveException();
        }
        if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getUser().getPassword())) {
            log.info("password matched");

            String jwt = generateTokenFromUserDetails(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            return Mapper.toJwtAuthenticationRepsonse(jwt, userDetails, refreshToken.getToken());
        }

        throw new UnauthorizedException("Password incorrect");
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        if(refreshToken.isEmpty()){
            throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
        }
        refreshTokenService.verifyExpiration(refreshToken.get());
        String token = generateTokenFromUserDetails(new CustomUserDetails(refreshToken.get().getUser()));
        return TokenRefreshResponse.builder()
                .accessToken(token)
                .refreshToken(requestRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    private String generateTokenFromUserDetails(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUser().getEmail());

        String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("roles", authorities);
        claims.put("userId", userDetails.getId());
        String subject = userDetails.getUsername();
        return jwtManager.generateToken(claims, subject);
    }

    public Boolean delete(Long id) {
        log.info("User service: Delete user id= " + id);
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id", "id", id)
        );

        userRepo.delete(user);
        log.info("User service: Delete successfully id= " + id);
        return true;
    }

    public UserTokenResponse getUserFromToken(String token) {
        log.info("Service: Get user from token: " + token);
        ConfirmationToken confirmationToken = null;
        if (!confirmationTokenService.isTokenValid(token)) {
            confirmationToken = confirmationTokenService.getToken(token).get();
        }
        return UserTokenResponse.builder()
                .username(confirmationToken.getUser().getUsername())
                .token(confirmationToken.getToken())
                .build();
    }

    @Transactional
    public String confirmToken(String token) {
        log.info("Confirm token");
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Confirmation Token", "token", token)
        );
        if (!confirmationTokenService.isTokenValid(token)) {
            confirmationTokenService.setConfirmDate(token);

            this.activeUser(confirmationToken.getUser().getUsername());
            return "Active User successfully";
        }
        throw new ResourceNotFoundException("Confirmation Token", "token", token);

    }

    public void activeUser(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        user.setIsActive(true);
    }
    public void loadCoin(Long userid, Long coin) {
        User user = userRepo.findById(userid).orElseThrow(
            () -> new UsernameNotFoundException("Username not found")
        );
        Long currentCoin = user.getCoin()+coin;
        user.setCoin(currentCoin);
        userRepo.save(user);
    }
    public void openPremium(Long userid) {
        User user = userRepo.findById(userid).orElseThrow(
            () -> new UsernameNotFoundException("Username not found")
        );
        Long userCoin = user.getCoin();
        if (userCoin < PremiumConstance.price){
            throw new InsufficientEx("account not enough coin");
        }
        user.setCoin(userCoin-PremiumConstance.price);
        //chỗ này set roles, xem hộ đoạn set role với Hiếu nhé
//        Đã set nhé
        Role userVipRole = roleRepo.findRoleByRoleName(ERole.ROLE_USER_VIP)
                .orElseGet(() -> roleRepo.save(Role.builder().roleName(ERole.ROLE_USER_VIP).build()));

        UserRole userRole = UserRole.builder()
                .role(userVipRole)
                .validUntil(LocalDateTime.now().plusMonths(1))
                .build();
        user.setUserRoles(new HashSet<>(Collections.singletonList(userRole)));
        userRole.setUser(user);
        userRepo.save(user);
    }

}
