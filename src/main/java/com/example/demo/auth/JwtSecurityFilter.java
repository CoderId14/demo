package com.example.demo.auth;

import com.example.demo.Service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


            final String requestTokenHeader = request.getHeader("Authorization");

            String username = null;
            String jwtToken = null;

            if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
                jwtToken = requestTokenHeader.substring(7);
                try{
                    username = jwtManager.getUsernameFromToken(jwtToken);
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

            if(username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null){
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
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
