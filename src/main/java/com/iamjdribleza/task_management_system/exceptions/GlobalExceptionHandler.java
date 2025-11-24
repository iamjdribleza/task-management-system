/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.exceptions;

import com.iamjdribleza.task_management_system.enums.ErrorCode;
import com.iamjdribleza.task_management_system.util.ProblemDetailUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * Global Exception class that is used to handle all types of exceptions.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Generates a response for a field/input that has an error
     *
     * @param e ResourceNotFoundException
     * @param request HttpServletRequest for request references like URL
     * @return ResponseEntity of ProblemDetail for more details on client
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                         HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetailUtil.details(
                "invalid-arguments",
                HttpStatus.BAD_REQUEST,
                "Invalid Arguments",
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getField(),
                HttpStatus.BAD_REQUEST.value(),
                "urn:problem:invoked-by:"+request.getRequestURI(),
                ErrorCode.INVALID_ARGUMENTS.name(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Generates a response for a resource that doesn't exist in the database
     *
     * @param e ResourceNotFoundException
     * @param request HttpServletRequest for request references like URL
     * @return ResponseEntity of ProblemDetail for more details on client
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> resourceNotFoundException(ResourceNotFoundException e,
                                                                   HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetailUtil.details(
                "resource-not-found",
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "urn:problem:invoked-by:"+request.getRequestURI(),
                ErrorCode.RESOURCE_NOT_FOUND.name(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * Generates a response for a resource that is already exists in the database
     *
     * @param e ResourceAlreadyExists
     * @param request HttpServletRequest for request references like URL
     * @return ResponseEntity of ProblemDetail for more details on client
     */
    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ProblemDetail> resourceAlreadyExists(ResourceAlreadyExists e,
                                                               HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetailUtil.details(
                "resource-conflict",
                HttpStatus.CONFLICT,
                "Resource Conflict",
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                "urn:problem:invoked-by:"+request.getRequestURI(),
                ErrorCode.RESOURCE_ALREADY_EXISTS.name(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    /**
     * Generates a response for authentication
     *
     * @param e BadCredentialsException
     * @param request HttpServletRequest for request references like URL
     * @return ResponseEntity of ProblemDetail for more details on client
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> badCredentialsException(BadCredentialsException e,
                                                                 HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetailUtil.details(
                "bad-credentials",
                HttpStatus.UNAUTHORIZED,
                "Bad Credentials",
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                "urn:problem:invoked-by:"+request.getRequestURI(),
                ErrorCode.BAD_CREDENTIALS.name(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}