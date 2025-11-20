/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Handles requests for user's operations.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    // Inject services
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * GET /api/v1/users/{refId}
     * Gets specific user using referend id.
     *
     * @param refId User's reference id.
     * @return ResponseEntity of user's details.
     */
    @GetMapping("/{refId}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID refId){
        return ResponseEntity.ok(userService.getUser(refId));
    }

    /**
     * GET /api/v1/users
     * Gets all Users
     *
     * @return ResponseEntity of user's list
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * POST /api/v1/users
     * Creates a new User
     *
     * @param userDto User's details to be saved.
     * @return HttpStatus of 204
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto){
        UserDto savedUser = userService.createUser(userDto);
        URI location = URI.create("/users/"+savedUser.referenceId());

        return ResponseEntity.created(location).build();
    }

    /**
     * PATCH /api/v1/users
     * Updates user's data
     *
     * @param userDto User's new details to replace the existing data
     * @return ResponseEntity of HttpStatus 201
     */
    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){
        userService.updateUser(userDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/v1/users/{refId}
     * Deletes a user
     *
     * @param refId User's reference id
     * @return ResponseEntity of HttpStatus 204
     */
    @DeleteMapping("/{refId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID refId){
        userService.deleteUser(refId);

        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/v1/users/deactivate
     * Deactivates a user's account
     *
     * @return ResponseEntity of HttpStatus 204
     */
    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivateUser(){
        userService.deactivateUser();

        return ResponseEntity.noContent().build();
    }
}
