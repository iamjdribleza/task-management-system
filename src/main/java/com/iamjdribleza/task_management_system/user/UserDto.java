/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.user;

import com.iamjdribleza.task_management_system.auth.AuthenticationEmailDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

/**
 * Stores User's data for service and controller rendering.
 *
 * @param referenceId Account's reference id
 * @param firstName Account's email
 * @param lastName Account's password
 * @param roles Account's list of roles
 * @param authentication Account's authentication details such as email and password
 *
 * @author iamjdribleza
 * @version 1.0
 */
public record UserDto(

        UUID referenceId,

        @NotBlank(message = "Blank first name")
        String firstName,

        @NotBlank(message = "Blank last name")
        String lastName,

        List<String> roles,

        @Valid
        AuthenticationEmailDto authentication
) {
}
