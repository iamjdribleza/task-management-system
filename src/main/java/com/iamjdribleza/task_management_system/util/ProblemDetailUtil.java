/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.io.IOException;
import java.net.URI;

/**
 * Utility class used for sending error response to the client
 *
 * @author iamjdribleza
 * @version 1.0
 */

@UtilityClass
public class ProblemDetailUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProblemDetail details(
            String type,
            HttpStatus httpStatus,
            String title,
            String detail,
            int status,
            String instance,
            String errorCode,
            String path){

        String uri = "https://example.com/errors/"+type;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setType(URI.create(uri));
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setStatus(status);
        problemDetail.setInstance(URI.create(instance));
        problemDetail.setProperty("code", errorCode);
        problemDetail.setProperty("path", path);
        problemDetail.setProperty("timeStamp", System.currentTimeMillis());

        return problemDetail;
    }

    public void writeDetailsAsJson(HttpServletResponse response, ProblemDetail details) throws IOException {
        response.setStatus(details.getStatus());
        response.setContentType("application/problem+json");

        objectMapper.writeValue(response.getWriter(), details);
    }
}
