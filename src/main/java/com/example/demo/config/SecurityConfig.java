package com.example.demo.config;

import com.example.demo.Service.CustomOAuth2UserService;
import com.example.demo.Service.CustomUserDetailsService;

import com.example.demo.auth.JwtSecurityFilter;
import com.example.demo.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.demo.auth.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.demo.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Data
//WebSecurityConfigurerAdapter deprecated in Spring 5.7
public class SecurityConfig {

    private final JwtSecurityFilter jwtSecurityFilter;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Value("${app.authorizedRedirectUris}")
    private String REDIRECT_URL;


    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.cors().and()
                .csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/",
                        "/api/auth/login/**",
                        "/api/auth/register/**",
                        "/api/auth/register/confirm",

                        "/api/user/**",
                "/oauth2/**",
                "api/auth/**"
                        ).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                        .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                ;
        http.addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling().
                authenticationEntryPoint(authenticationEntryPoint);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    similar with AuthenticationManagerBuilder
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
