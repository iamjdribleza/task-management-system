/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import com.iamjdribleza.task_management_system.api.ResponseToken;
import com.iamjdribleza.task_management_system.exceptions.ResourceNotFoundException;
import com.iamjdribleza.task_management_system.jwt.JwtTokenServiceImpl;
import com.iamjdribleza.task_management_system.user.User;
import com.iamjdribleza.task_management_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Class implementation of AuthService
 * Perform user's authentication, send email verification
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenServiceImpl jwtTokenServiceImpl;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Logs in and authenticate a user.
     * Generates and sets token if authentication is successful.
     *
     * @param credentials User's credentials such as email and password.
     * @return ResponseToken of access token and expiration.
     */
    @Override
    public ResponseToken authenticate(AuthenticationRequestDto credentials) {
        ResponseToken token = null;
        try{

            // Authenticate user with the given credentials
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password())
            );

            // Generate and assign token
            token = jwtTokenServiceImpl.generateToken(authentication);

        }catch (BadCredentialsException bce){
            throw new BadCredentialsException("Invalid email address or password");
        }

        return token;
    }

    /**
     * Gets authenticated user from security context and retrieve user from the database.
     *
     * @return Current or authenticated user.
     */
    @Override
    public User getAuthenticatedUser() {
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Authentication auth = authenticationRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        return auth.getUser();
    }

    /**
     * Sends email verification link for password reset.
     *
     * @param authenticationEmailDto user's email address.
     */
    @Override
    public void sendEmailVerificationLink(AuthenticationEmailDto authenticationEmailDto) {
        Authentication authentication = authenticationRepository.findByEmail(authenticationEmailDto.email())
                .orElseThrow(() -> new ResourceNotFoundException("email"));

        // Send verification email here
    }

    /**
     * Updates user's password from the database.
     *
     * @param authenticationPasswordDto User's password details.
     */
    @Override
    public void updatePassword(AuthenticationPasswordDto authenticationPasswordDto) {
        // Get authenticated user
        User user = this.getAuthenticatedUser();

        // Get authentication from user
        Authentication authentication = user.getAuthentication();

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(authenticationPasswordDto.password());

        // Set the new password
        authentication.setPassword(encryptedPassword);

        authenticationRepository.save(authentication);
    }
}