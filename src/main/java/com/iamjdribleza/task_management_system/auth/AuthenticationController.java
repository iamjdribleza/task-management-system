/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import com.iamjdribleza.task_management_system.api.ResponseToken;
import com.iamjdribleza.task_management_system.jwt.JwtTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests for authentication operations.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    // Inject services
    private final AuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;

    public static final long EXPIRES_IN_SEVEN_DAYS = 7 * 86400;

    /**
     * Logs in a user.
     *
     * @param credentials user's credentials such as email and password.
     * @return ResponseEntity details of authenticated user.
     */
    @PostMapping
    public ResponseEntity<ResponseToken> authenticate(@Valid @RequestBody AuthenticationRequestDto credentials) {
        ResponseToken responseToken = authenticationService.authenticate(credentials);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", responseToken.accessToken())
                .httpOnly(true)
                .secure(false)  // true in production HTTPS
                .path("/api/v1/auth")
                .maxAge(EXPIRES_IN_SEVEN_DAYS)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseToken);
    }

    /**
     * Refreshes expired token.
     *
     * @param refreshToken token stored in cookies.
     * @return ResponseEntity of token with expiration.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseToken> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        ResponseToken responseToken = jwtTokenService.refreshToken(refreshToken);

        return ResponseEntity.ok(responseToken);
    }

    /**
     * Sends email verification link for password reset.
     *
     * @param authenticationEmailDto User's authentication details.
     * @return ResponseEntity with HttpStatus 204.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> sendEmailVerificationLink(@Valid @RequestBody AuthenticationEmailDto authenticationEmailDto) {
        this.authenticationService.sendEmailVerificationLink(authenticationEmailDto);

        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/v1/auth
     * Updates user's password
     *
     * @param authenticationPasswordDto Authentication's password details
     * @return ResponseEntity with HttpStatus 204.
     */
    @PatchMapping
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody AuthenticationPasswordDto authenticationPasswordDto) {
        authenticationService.updatePassword(authenticationPasswordDto);

        return ResponseEntity.noContent().build();
    }
}