/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.jwt;

import com.iamjdribleza.task_management_system.api.ResponseToken;
import org.springframework.security.core.Authentication;

/**
 * Jwt Token Service
 *
 * @author iamjdribleza
 * @version 1.0
 */
public interface JwtTokenService {
    ResponseToken generateToken(Authentication auth);
    ResponseToken refreshToken(String refreshToken);
    String extractUsernameFromToken(String token);
}