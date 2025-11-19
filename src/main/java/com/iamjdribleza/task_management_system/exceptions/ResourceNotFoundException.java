/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.exceptions;

import java.io.Serial;

/**
 * Exception class for resource that is not found in the database.
 *
 * @author iamjdribleza
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message){
        super(message);
    }
}