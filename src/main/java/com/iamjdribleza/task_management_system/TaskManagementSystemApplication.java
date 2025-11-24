/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TaskManagementSystemApplication {

    private final Logger LOGGER = LoggerFactory.getLogger(TaskManagementSystemApplication.class);

    // Get application name from the configuration
    @Value("${spring.application.name}")
    private String appName;

    // Get server port from the configuration
	@Value("${server.port}")
	private int serverPort;

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementSystemApplication.class, args);
	}

    /**
     * Logs when application started
     */
	@EventListener(ApplicationReadyEvent.class)
	public void initApp(){
        LOGGER.info("{} has started at port {}", appName, serverPort);
	}
}
