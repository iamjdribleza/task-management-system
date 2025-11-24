/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * Stores incoming Authentication data for service and controller rendering.
 *
 * @param referenceId account's reference id
 * @param email account's email
 * @param password account's password
 *
 * @author iamjdribleza
 * @version 1.0
 */
public record AuthenticationRequestDto(
        UUID referenceId,

        @NotBlank(message = "Blank email address")
        String email,

        @NotBlank(message = "Blank password")
        String password
){}