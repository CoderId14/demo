package com.example.demo.Service.user;


import com.example.demo.Repository.role.RoleRepo;
import com.example.demo.Repository.user.UserRepo;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.auth.user.OAuth2UserInfo;
import com.example.demo.auth.user.OAuth2UserInfoFactory;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.supports.AuthProvider;
import com.example.demo.entity.supports.ERole;
import com.example.demo.exceptions.OAuth2AuthenticationProcessingException;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.example.demo.Utils.AppConstants.ROLE_USER;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements Serializable {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User)
            throws OAuth2AuthenticationProcessingException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        Optional<User> userOptional = userRepo.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()){
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))){
                throw new OAuth2AuthenticationProcessingException("Use your " + user.getProvider() +" account to login");
            }
            user = updateExistingUser(user, oAuth2UserInfo);


        }else{
            user = signUpNewUser(oAuth2UserRequest,oAuth2UserInfo);
        }
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

//    Bổ sung DTO
    private User signUpNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo){
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepo.findRoleByRoleName(ERole.ROLE_USER).orElseThrow(
                        () -> new ResourceNotFoundException("Role", "Role name", ROLE_USER)
                ));
        User user = User.builder()
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .username(oAuth2UserInfo.getEmail())
                .email(oAuth2UserInfo.getEmail())
                .roles(roles)
                .avatar(oAuth2UserInfo.getImageUrl())
                .isActive(true)
                .build();
        return userRepo.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setAvatar(oAuth2UserInfo.getImageUrl());
        return userRepo.save(existingUser);
    }
}
