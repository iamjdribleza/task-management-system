/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.task;

import com.iamjdribleza.task_management_system.auth.AuthService;
import com.iamjdribleza.task_management_system.exceptions.PermissionDeniedException;
import com.iamjdribleza.task_management_system.exceptions.ResourceNotFoundException;
import com.iamjdribleza.task_management_system.mapper.TaskMapper;
import com.iamjdribleza.task_management_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Task Service implementation
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@Transactional(rollbackFor = ResourceNotFoundException.class)
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final AuthService authService;
    private final TaskMapper taskMapper;
    private final JwtDecoder jwtDecoder;

    /**
     * Retrieves all tasks for the current date.
     *
     * @param pageOffset Page offset.
     * @return Task's details page.
     */
    @Override
    public Page<TaskDto> getTodaysTasks(String token, int pageOffset) {

        // Get authenticated user
        User user = authService.getAuthenticatedUser();
        LocalDate today = LocalDate.now();

        Pageable pageable = PageRequest.of(pageOffset, 10);
        Page<Task> todaysTasks = taskRepository.findByUserAndEventDate(user, pageable, today);

        return todaysTasks.map(taskMapper::toDto);
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return Pages of all tasks for logged-in user.
     */
    @Override
    public Page<TaskDto> getAllTasks() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("date").descending());

        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    /**
     * Gets a task using reference id.
     *
     * @param refId Task's reference id.
     * @return Task's details.
     * @throws ResourceNotFoundException if task is not found.
     */
    @Override
    public TaskDto getTask(UUID refId) {

        // Get authenticated user
        User user = authService.getAuthenticatedUser();

        // Retrieve task
        Task task = taskRepository.findByRefId(refId)
                .orElseThrow(() -> new ResourceNotFoundException("refId"));

        // Check if user has permission to access the task
        if (!userHasPermission(task))
            // Throw AccessDeniedException
            throw new PermissionDeniedException("Permission denied");

        return taskMapper.toDto(task);
    }

    /**
     * Creates a task and inserts into database.
     *
     * @param taskDto Task's details.
     * @return Task's reference id.
     * @throws ResourceNotFoundException if user is not found using email address.
     */
    @Transactional
    @Override
    public String createTask(String token, TaskDto taskDto) {
        Task task = taskMapper.toTask(taskDto);

        // Get authenticated user
        User user = authService.getAuthenticatedUser();

        // Set user to task
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        // Return task's reference id
        return savedTask.getReferenceId().toString();
    }

    /**
     * Updates a task using reference id and saves to database.
     *
     * @param refId Task's reference id.
     * @param taskDto Task's new details to replace the existing data.
     * @throws ResourceNotFoundException if task is not found.
     */
    @Transactional
    @Override
    public void updateTask(UUID refId, TaskDto taskDto) {
        Task task = taskRepository.findByRefId(refId)
                .orElseThrow(() -> new ResourceNotFoundException("refId"));

        // Check if user has permission to delete a task
        if (!userHasPermission(task))
            // Throw AccessDeniedException
            throw new PermissionDeniedException("Permission denied");

        task.setDescription(taskDto.description());
        task.setEventDate(taskDto.eventDate());
        task.setPriority(taskDto.priority());

        taskRepository.save(task);
    }

    /**
     * Deletes a task using reference id from the database.
     *
     * @param refId Task's reference id.
     * @throws ResourceNotFoundException if task is not found.
     */
    @Transactional
    @Override
    public void deleteTask(UUID refId) {

        Task task = taskRepository.findByRefId(refId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Check if user has permission to delete a task
        if (!userHasPermission(task))
            // Throw AccessDeniedException
            throw new PermissionDeniedException("Permission denied");

        taskRepository.delete(task);
    }

    /**
     * Compares task user's reference id and authenticated user's reference id
     *
     * @param task To get user's reference
     * @return true if user is the owner of the task, false otherwise
     */
    private boolean userHasPermission(Task task){
        // Authenticated user
        User user = authService.getAuthenticatedUser();

        // Get task user's reference id
        UUID taskUserReferenceId = task.getUser().getReferenceId();

        return user.getReferenceId().equals(taskUserReferenceId);
    }
}