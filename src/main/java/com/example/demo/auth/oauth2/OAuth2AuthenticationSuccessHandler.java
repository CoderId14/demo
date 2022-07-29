package com.example.demo.auth.oauth2;

import com.example.demo.Service.impl.UserService;
import com.example.demo.Utils.CookieUtils;
import com.example.demo.auth.JwtManager;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.config.AppConfig;
import com.example.demo.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtManager jwtManager;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final AppConfig appConfig;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("Login on success");

        String targetUrl = determineTargetUrl(request,response,authentication);

//        response.setHeader("Authorization", "Bearer " + targetUrl);
        clearAuthenticationAttributes(request,response);

//        targetUrl = "http://localhost:3000/oauth2/callback";

        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
//        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())){
//            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
//        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();


        Map<String, Object> claims = new HashMap<>();
        claims.put("email", customUserDetails.getUsername());

        String authorities = customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("roles", authorities);
        claims.put("userId", customUserDetails.getId());
        String subject = customUserDetails.getUser().getEmail();
        String token = jwtManager.generateToken(claims,subject);
        String targetUrl = "http://localhost:3000/oauth2/callback";
        CookieUtils.addCookie(response,"token",token,36000);
        return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();

    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri){
        URI clientRedirectUri = URI.create(uri);
        return appConfig.getAuthorizedRedirectUris().stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())&&
                    authorizedURI.getPort()== clientRedirectUri.getPort()){
                        return true;
                    }
                    return false;
                });
    }

}
