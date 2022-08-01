package com.example.demo.Service.impl;

import com.example.demo.Repository.RoleRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.CustomUserDetailsService;
import com.example.demo.Service.IEmailSender;
import com.example.demo.Service.IUserService;
import com.example.demo.Utils.MailBuilder;
import com.example.demo.auth.JwtManager;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.request.ChangePasswordDto;
import com.example.demo.dto.request.ForgotPasswordDto;
import com.example.demo.dto.request.LoginDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.dto.response.ChangePasswordResponse;
import com.example.demo.dto.response.ForgotPasswordResponse;
import com.example.demo.dto.response.JwtAuthenticationResponse;
import com.example.demo.dto.response.UserTokenResponse;
import com.example.demo.entity.ConfirmationToken;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.supports.AuthProvider;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.user.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.Utils.Constant.ROLE_USER;
import static com.example.demo.Utils.Vadilate.isEmail;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final String LINK_VERIFY = "http://localhost:8080/api/auth/signUp/confirm?token=";
    private final String LINK_FORGOT_PASSWORD = "http://localhost:8080/api/user/forgot-password?token=";

    private final JwtManager jwtManager;

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final ConfirmationTokenService confirmationTokenService;

    private final IEmailSender emailService;

    private final MailBuilder mailBuilder;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;


    public User getUser(String username){
        return userRepo.findByUsername(username).orElseThrow(
        () -> new ResourceNotFoundException("User", "Username", username)
        );
    }
//    Test
    public UserDto addUser(SignUpDto signUpDto){

        if(userRepo.existsByUsername(signUpDto.getUsername())){
            throw new ResourceExistsException("User", "Username", signUpDto.getUsername());
        }
        if(userRepo.existsByEmail(signUpDto.getEmail())){
            throw new ResourceExistsException("User", "Email", signUpDto.getEmail());
        }
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepo.findRoleByRoleName(ROLE_USER).orElseGet(() ->roleRepo.save(
                        Role.builder().roleName("ROLE_USER").build())));


        User user = User.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .roles(roles)
                .isActive(true)
                .build();
        userRepo.save(user);
        return Mapper.toUserDto(user);
    }

    public ChangePasswordResponse updatePasswordByToken(ChangePasswordDto changePasswordDto){
        log.info("Service: updatePassword");
        User user;
        ConfirmationToken token = confirmationTokenService.getToken(changePasswordDto.getToken()).orElseThrow(
                () -> new IllegalStateException("Token not found")
        );

        String usernameOrEmail = changePasswordDto.getUsernameOrEmail();
        if(isEmail(usernameOrEmail)){
            user = userRepo.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Email", usernameOrEmail)
            );
        }
        else{
            user = userRepo.findByUsername(usernameOrEmail).orElseThrow(
                    ()-> new ResourceNotFoundException("User", "Username", usernameOrEmail)
            );
        }
        if(user.getId() == token.getUser().getId()){
            user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
            userRepo.save(user);
            return ChangePasswordResponse.builder()
                    .username(usernameOrEmail)
                    .password(changePasswordDto.getPassword())
                    .build();
        }

        throw new RuntimeException("Token invalid");

    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordDto forgotPasswordDto){
        log.info("forgotPassword Service");
        User user;
        String usernameOrEmail = forgotPasswordDto.getUsernameOrEmail();
        if(isEmail(usernameOrEmail)){
            user = userRepo.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new ResourceNotFoundException("User", "Email", usernameOrEmail)
            );
        }
        else{
            user = userRepo.findByUsername(usernameOrEmail).orElseThrow(
                    ()-> new ResourceNotFoundException("User", "Username", usernameOrEmail)
            );
        }
        String token = confirmationTokenService.generateConfirmationToken(user);

        String link = LINK_FORGOT_PASSWORD + token;
        emailService.send(user.getEmail(),
                mailBuilder.buildEmailForgotPassword(user.getUsername(),link));
        return new ForgotPasswordResponse("Check your email: " + user.getEmail(), token);
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
    public UserDto signUp(SignUpDto signUpDto) {
        log.info("Sign up Service");
        if(userRepo.existsByUsername(signUpDto.getUsername())){
            throw new ResourceExistsException("User", "Username", signUpDto.getUsername());
        }
        if(userRepo.existsByEmail(signUpDto.getEmail())){
            throw new ResourceExistsException("User", "Email", signUpDto.getEmail());
        }
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepo.findRoleByRoleName(ROLE_USER).orElseThrow(
                        () -> new ResourceNotFoundException("Role", "Role name", ROLE_USER)
                ));
        User user = User.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .isActive(false)
                .roles(roles)
                .build();
        userRepo.save(user);

        String token = confirmationTokenService.generateConfirmationToken(user);

        String link = LINK_VERIFY + token;
        emailService.send(signUpDto.getEmail(),
                mailBuilder.buildEmailSignUp(signUpDto.getUsername(),link));
        return Mapper.toUserDto(user);

    }

    @Override
    public JwtAuthenticationResponse login(LoginDto loginDto) {
        log.info("Login service");
        CustomUserDetails userDetails;
        try {
            userDetails = new CustomUserDetails(userRepo.findByUsername(loginDto.getUsername()).get());

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        if (userDetails.getUser().getIsActive() &&
                passwordEncoder.matches(loginDto.getPassword(), userDetails.getUser().getPassword())) {
            log.info("password matched");

            Map<String, Object> claims = new HashMap<>();
            claims.put("email", userDetails.getUser().getEmail());

            String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            claims.put("roles", authorities);
            claims.put("userId", userDetails.getId());
            String subject = userDetails.getUsername();
            String jwt = jwtManager.generateToken(claims, subject);
            return Mapper.toJwtAuthenticationRepsonse(jwt, userDetails);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
    }

    public Boolean delete(Long id ){
        log.info("User service: Delete user id= " + id);
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id", "id", id)
        );

        userRepo.delete(user);
        log.info("User service: Delete successfully id= " + id);
        return true;
    }

    public UserTokenResponse getUserFromToken(String token){
        log.info("Service: Get user from token: " + token);
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
                () -> new IllegalStateException("Token not found")
        );
        return UserTokenResponse.builder()
                .username(confirmationToken.getUser().getUsername())
                .token(confirmationToken.getToken())
                .build();
    }

    @Transactional
    public String confirmToken(String token){
        log.info("Confirm token");
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
                () -> new IllegalStateException("Token not found")
        );
        if(!confirmationTokenService.isTokenValid(token)){
            confirmationTokenService.setConfirmDate(token);

            this.activeUser(confirmationToken.getUser().getUsername());
            return "confirmed";
        }
        throw new IllegalStateException("Token expired");
    }

    public void activeUser(String username){
        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        user.setIsActive(true);
    }


}
