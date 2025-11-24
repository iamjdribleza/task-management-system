/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.task;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * Task Service
 *
 * @author iamjdribleza
 * @version 1.0
 */
public interface TaskService {
    Page<TaskDto> getAllTasks();
    Page<TaskDto> getTodaysTasks(String token, int pageOffset);
    String createTask(String token, TaskDto taskDto);
    TaskDto getTask(UUID refId);
    void updateTask(UUID refId, TaskDto taskDto);
    void deleteTask(UUID refId);
}