/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import com.iamjdribleza.task_management_system.api.ResponseToken;
import com.iamjdribleza.task_management_system.user.User;

/**
 * Authentication Service
 *
 * @author iamjdribleza
 * @version 1.0
 */
public interface AuthService {
    ResponseToken authenticate(AuthDto credentials);
    void sendEmailVerificationLink(EmailDto emailDto);
    User getAuthenticatedUser();
}