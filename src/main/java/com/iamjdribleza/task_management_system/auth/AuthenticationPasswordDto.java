/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationPasswordDto(
        @NotBlank(message = "Blank Password")
        String password
) {
}