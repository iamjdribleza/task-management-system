/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Stores user's Authentication data for service and controller rendering.
 *
 * @param email account's email
 *
 * @author iamjdribleza
 * @version 1.0
 */
public record AuthenticationEmailDto(
        @NotBlank(message = "Blank email address")
        String email
) {
}
