/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.exceptions;

import java.io.Serial;

/**
 * Permission Denied Exception
 * Catches exception that is not allowed to user's actions.
 *
 * @author iamjdribleza
 * @version 1.0
 */
public class PermissionDeniedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public PermissionDeniedException(String message){
        super(message);
    }
}
