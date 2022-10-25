package com.example.demo.auth;

import com.example.demo.Service.CustomUserDetailsService;
import com.example.demo.Utils.CookieUtils;
import com.example.demo.auth.user.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;

    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*" );


        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With");
        response.setHeader("Content-Type", "application/json");
        if (request.getHeader(HttpHeaders.ORIGIN) != null
                && request.getMethod().equals(HttpMethod.OPTIONS.name())
                && request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null) {
            log.debug("Received an OPTIONS pre-flight request.");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
            String requestTokenHeader = request.getHeader("Authorization");
            if( requestTokenHeader == null && CookieUtils.getCookie(request, "token").isPresent()){
                requestTokenHeader ="Bearer " + CookieUtils.getCookie(request, "token").get().getValue();
            }
            String email = null;
            String jwtToken = null;

            if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
                jwtToken = requestTokenHeader.substring(7);
                try{
                    email = jwtManager.getEmailFromToken(jwtToken);
                }
                catch (IllegalArgumentException exception){
                    log.info("Unable to get JWT token");
                }
                catch(ExpiredJwtException exception){
                    log.info("JWT token has expired");
                }
            }
            else{
                log.warn("JWT Token does not start with Bearer");
            }

            if(email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null){
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
                if(jwtManager.validatedToken(jwtToken, customUserDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                            customUserDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }

            filterChain.doFilter(request,response);
    }
}
