/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Stores Authentication data for service and controller rendering
 *
 * @param email User's email address
 * @author iamjdribleza
 * @version 1.0
 */
public record EmailDto(
        @NotBlank(message = "Blank email address")
        String email
) {}
