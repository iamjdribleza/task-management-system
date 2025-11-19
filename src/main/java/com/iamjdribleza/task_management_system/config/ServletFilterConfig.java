/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.config;

import com.iamjdribleza.task_management_system.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet Filter Configuration
 * - Includes bean for logging http requests and response
 *
 * @author iamjdribleza
 * @version 1.0
 */
@Configuration
public class ServletFilterConfig {

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterBean(){
        FilterRegistrationBean<LoggingFilter> loggingFilterBean = new FilterRegistrationBean<>();
        loggingFilterBean.setFilter(new LoggingFilter());
        loggingFilterBean.addUrlPatterns("/*");

        return loggingFilterBean;
    }
}