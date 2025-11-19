/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.filter;

import com.iamjdribleza.task_management_system.enums.ErrorCode;
import com.iamjdribleza.task_management_system.jwt.JwtTokenService;
import com.iamjdribleza.task_management_system.util.ProblemDetailUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Authentication Filter
 * Checks token on every request and authenticate.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_BASE_PATH = "/api/v1/auth";

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Check request and allowed endpoints
        if (
                request.getMethod().equalsIgnoreCase("POST")
                        && List.of(
                                AUTH_BASE_PATH,
                                AUTH_BASE_PATH+"/forgot-password",
                                AUTH_BASE_PATH+"/refresh"
                        ).contains(request.getRequestURI())
        ){
            filterChain.doFilter(request, response);
            return;
        }

        // Get authorization
        String authorization = request.getHeader("Authorization");

        // Check authorization
        if (authorization != null && authorization.startsWith("Bearer ")){
            ProblemDetail responseError = ProblemDetailUtil.details(
                    HttpStatus.FORBIDDEN,
                    "Token Not Found",
                    "No token found",
                    HttpStatus.FORBIDDEN.value(),
                    ErrorCode.REQUEST_FORBIDDEN.name(),
                    request.getRequestURI()
            );

            // Send error as JSON
            response.getWriter().write(responseError.toString());
            return;
        }

        // Get token from authorization by omitting Bearer prefix
        String token = request.getHeader("Authorization").substring(7);

        String email = "";

        // This try block will check if token has already expired.
        try{
            // Get email from extracted token(claim)
            email = jwtTokenService.extractUsernameFromToken(token);

            // Set Authentication
            this.setAuthentication(email, request);

            // Continue chain
            filterChain.doFilter(request, response);
        }catch (JwtException e){
            // Sets attribute and return 401 Unauthorized if token is already expired
            request.setAttribute("jwtExpired", true);
        }
    }


    /**
     * Sets authentication to security context
     *
     * @param email User's email address
     * @param request HttpServletRequest for request's references
     */
    private void setAuthentication(String email, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Set authentication
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Set authentication to SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}