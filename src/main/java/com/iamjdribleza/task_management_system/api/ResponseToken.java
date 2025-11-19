/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.api;

/**
 * Used as response when authentication is successful.
 *
 * @author iamjdribleza
 * @version 1.0
 */
public record ResponseToken(
    String accessToken,
    long expiresIn
){}