/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.jwt;

import com.iamjdribleza.task_management_system.api.ResponseToken;
import com.iamjdribleza.task_management_system.auth.AuthenticationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Class implementation of JwtService
 * Used to serve token with JWT
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public static final long EXPIRES_IN_ONE_HOUR = 3600; // 1 hour

    /**
     * Generates authentication token
     *
     * @param auth authenticated user
     * @return String generated token
     */
    @Override
    public ResponseToken generateToken(Authentication auth){
        Instant now = Instant.now();
        String email = ((AuthenticationDetails) auth.getPrincipal()).getUsername();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRES_IN_ONE_HOUR))
                .subject(auth.getName())
                .claim("email", email)
                .build();

        // Set token header algorithm
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new ResponseToken(token, EXPIRES_IN_ONE_HOUR);
    }

    /**
     * Refreshes token by generating a new one
     *
     * @param refreshToken token from cookies
     * @return ResponseToken new token with expiration
     */
    @Override
    public ResponseToken refreshToken(String refreshToken){
        Jwt jwt = jwtDecoder.decode(refreshToken);
        String username = jwt.getClaimAsString("email");

        return generateToken(new UsernamePasswordAuthenticationToken(username, null));
    }

    /**
     * Extracts username from token.
     *
     * @param token User's token to be extracted.
     * @return Email address as username.
     */
    @Override
    public String extractUsernameFromToken(String token){
        Jwt jwt = jwtDecoder.decode(token);

        return jwt.getClaimAsString("email");
    }
}