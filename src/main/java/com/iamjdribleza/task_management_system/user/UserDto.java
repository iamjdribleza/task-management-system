/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.user;

import com.iamjdribleza.task_management_system.auth.AuthDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * Stores User's data for service and controller rendering.
 *
 * @param referenceId account's reference id
 * @param firstName account's email
 * @param lastName account's password
 * @param roles account's list of roles
 * @param auth account's authentication details such as email and password
 *
 * @author iamjdribleza
 * @version 1.0
 */
public record UserDto(

        String referenceId,

        @NotBlank(message = "Blank first name")
        String firstName,

        @NotBlank(message = "Blank last name")
        String lastName,

        List<SimpleGrantedAuthority> roles,

        @Valid
        AuthDto auth
) {
}
