/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * Utility class used for sending error response to the client
 *
 * @author iamjdribleza
 * @version 1.0
 */

@UtilityClass
public class ProblemDetailUtil {
    public ProblemDetail details(
            HttpStatus httpStatus,
            String title,
            String detail,
            int status,
            String errorCode,
            String uri){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setStatus(status);
        problemDetail.setProperty("code", errorCode);
        problemDetail.setProperty("path", uri);

        return problemDetail;
    }
}
