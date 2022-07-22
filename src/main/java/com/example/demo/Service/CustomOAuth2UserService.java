package com.example.demo.Service;


import com.example.demo.Repository.UserRepo;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.auth.user.OAuth2UserInfo;
import com.example.demo.auth.user.OAuth2UserInfoFactory;
import com.example.demo.entity.User;
import com.example.demo.entity.supports.AuthProvider;
import com.example.demo.exceptions.OAuth2AuthenticationProcessingException;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return super.loadUser(userRequest);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User)
            throws OAuth2AuthenticationProcessingException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        User user = userRepo.findByEmail(oAuth2UserInfo.getEmail()).orElseThrow(
                () -> new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        );

        if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))){
            throw new OAuth2AuthenticationProcessingException("Use your " + user.getProvider() +" account to login");
        }
        user = signUpNewUser(oAuth2UserRequest, oAuth2UserInfo);

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

//    Bổ sung DTO
    private User signUpNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo){
        User user = User.builder()
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .username(oAuth2UserInfo.getUsername())
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .avatar(oAuth2UserInfo.getImageUrl())
                .build();
        return userRepo.save(user);
    }
}
