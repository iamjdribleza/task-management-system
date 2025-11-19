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
                HttpStatus.BAD_REQUEST,
                "INVALID_ARGUMENTS",
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getField(),
                400,
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
                HttpStatus.NOT_FOUND,
                "Not Found",
                e.getMessage(),
                404,
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
                HttpStatus.CONFLICT,
                "Conflict",
                e.getMessage(),
                409,
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
                HttpStatus.NOT_FOUND,
                "Invalid Credentials",
                e.getMessage(),
                404,
                ErrorCode.INVALID_CREDENTIALS.name(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}