package com.example.demo.auth.user;

import com.example.demo.exceptions.OAuth2AuthenticationProcessingException;

import java.util.Map;

import static com.example.demo.entity.supports.AuthProvider.google;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        if(registrationId.equalsIgnoreCase(google.toString())){
            return new GoogleOAuth2UserInfo(attributes);
        }
        else{
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId +" is not support yet");
        }
    }
}
