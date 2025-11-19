/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.exceptions;

/**
 * Exception class for resource that is already exists in the database.
 *
 * @author iamjdribleza
 * @version 1.0
 */
public class ResourceAlreadyExists extends RuntimeException{
    private static final long SerialVersionUID = 1L;

    public ResourceAlreadyExists(String message){
        super(message);
    }
}