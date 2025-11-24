/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.task;

import com.iamjdribleza.task_management_system.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Stores Task details for service and controller rendering.
 *
 * @param referenceId task reference id
 * @param description task's details
 * @param eventDate task's date occurs
 * @param timeAgo time passed upon task creation
 * @param priority task's priority
 *
 * @author iamjdribleza
 * @version 1.0
 */
public record TaskDto(
        UUID referenceId,

        @NotBlank(message = "Blank description")
        String description,

        @NotNull(message = "Blank event date")
        LocalDate eventDate,

        String timeAgo,

        Priority priority
) {}