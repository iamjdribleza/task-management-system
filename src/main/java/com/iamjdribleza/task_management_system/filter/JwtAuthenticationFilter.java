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
    private static final String USER_BASE_PATH = "/api/v1/users";
    private static final List<String> WHITE_LISTS = List.of(
            AUTH_BASE_PATH,
            AUTH_BASE_PATH+"/forgot-password",
            AUTH_BASE_PATH+"/refresh",
            USER_BASE_PATH
    );

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Check request and allowed endpoints
        if (request.getMethod().equalsIgnoreCase("POST") && WHITE_LISTS.contains(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }


        // Get authorization
        String authorization = request.getHeader("Authorization");

        // Check if token is present
        try {
            boolean isTokenPresent = authorization.split(" ")[1] == null;
        }catch (ArrayIndexOutOfBoundsException e){
            setProblemDetails(request, response);
            return;
        }

        // Check authorization
        if (authorization == null || !authorization.startsWith("Bearer ")){
            setProblemDetails(request, response);
            return;
        }

        // Get token from authorization by omitting Bearer prefix
        String token = authorization.substring(7);

        // Initialize email as blank
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
            // This will be handled in front-end using interceptor(response) and will check header attribute
            // as token expired, it will automatically send request for new token
            request.setAttribute("token_expired", true);
            System.out.println("Token expired");
            return;
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

    /**
     * Sets problem details that is sent to client when a token error occurs
     *
     * @param request HttpServletRequest for request references like uri
     * @param response HttpServletResponse for response references like writer
     * @throws IOException If response writer gets an error
     */
    private void setProblemDetails(HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {

        ProblemDetail responseError = ProblemDetailUtil.details(
                "token-not-found",
                HttpStatus.FORBIDDEN,
                "Token Not Found",
                "No token found",
                HttpStatus.FORBIDDEN.value(),
                "urn:problem:missing-token",
                ErrorCode.REQUEST_FORBIDDEN.name(),
                request.getRequestURI()
        );

        // Send error as JSON
        ProblemDetailUtil.writeDetailsAsJson(response, responseError);
    }
}