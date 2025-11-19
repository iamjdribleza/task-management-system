/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.task;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

/**
 * Handles requests for Task operations.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * GET /api/v1/tasks
     * Gets all tasks
     *
     * @return ResponseEntity of all tasks with page details.
     */
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * GET /api/v1/tasks?page=1
     * Gets all today's tasks.
     *
     * @param pageOffset Page offset.
     * @return ResponseEntity of tasks on current date with page details.
     */
    @GetMapping("/today")
    public ResponseEntity<Page<TaskDto>> getTodaysTasks(@RequestParam int pageOffset, HttpServletRequest request){
        String token = request.getHeader("Authorization").split(" ")[1];
        return ResponseEntity.ok(taskService.getTodaysTasks(token, pageOffset));
    }

    /**
     * GET /api/v1/tasks/{refId}
     * Gets specific task using reference id.
     *
     * @param refId User's reference id.
     * @return ResponseEntity of a task.
     */
    @GetMapping("/{refId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable UUID refId){
        return ResponseEntity.ok(taskService.getTask(refId));
    }

    /**
     * POST /api/v1/tasks
     * Creates a task
     *
     * @param taskDto Task's details.
     * @return ResponseEntity with HttpStatus 204.
     */
    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto taskDto, HttpServletRequest request){
        String token = request.getHeader("Authorization").split(" ")[1];

        String refId = taskService.createTask(token, taskDto);
        URI location = URI.create("/tasks/"+refId);

        return ResponseEntity.created(location).build();
    }

    /**
     * PATCH /api/v1/tasks/{refId}
     * Updates a task
     *
     * @param refId User's reference id.
     * @param taskDto Task's new details to replace the old data.
     * @return ResponseEntity with HttpStatus 204.
     */
    @PatchMapping("/{refId}")
    public ResponseEntity<?> updateTask(@PathVariable UUID refId, @RequestBody TaskDto taskDto){
        taskService.updateTask(refId, taskDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/v1/tasks/{refId}
     * Deletes a task
     *
     * @param refId User's reference id.
     * @return ResponseEntity with HttpStatus 204.
     */
    @DeleteMapping("/{refId}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID refId){
        taskService.deleteTask(refId);
        return ResponseEntity.noContent().build();
    }
}