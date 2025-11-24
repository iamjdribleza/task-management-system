/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.user;

import com.iamjdribleza.task_management_system.auth.AuthenticationService;
import com.iamjdribleza.task_management_system.enums.AccountStatus;
import com.iamjdribleza.task_management_system.exceptions.ResourceAlreadyExists;
import com.iamjdribleza.task_management_system.exceptions.ResourceNotFoundException;
import com.iamjdribleza.task_management_system.mapper.UserMapper;
import com.iamjdribleza.task_management_system.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Class implementation of UserService for user's data operations.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@RequiredArgsConstructor

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    /**
     * Retrieves a user using reference id from the database.
     *
     * @param refId User's reference id.
     * @return User's details.
     * @throws ResourceNotFoundException if user is not found.
     */
    @Override
    public UserDto getUser(UUID refId) {
        return userRepository.findByReferenceId(refId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("refId"));
    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of user's details.
     */
    @Override
    public List<UserDto> getUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    /**
     * Creates a user and saves to database.
     *
     * @param userDto User's new details to be saved.
     * @return User's newly saved data.
     * @throws ResourceAlreadyExists if a user's email is already in the database
     */
    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {

        // Check if email is already exists in the database
        if (userRepository.existsByAuthenticationEmail(userDto.authentication().email()))
            // Throw exception
            throw new ResourceAlreadyExists("Email already in use");

        // If email doesn't exist, then map user's details(dto) to a User object
        User newUser = userMapper.toUser(userDto);

        // Set user's reference id
        newUser.setReferenceId(UUID.randomUUID());

        // Set roles as USER for new user
        newUser.getRoles().add(Role.USER.name());

        // Set Auth's reference id
        newUser.getAuthentication().setReferenceId(UUID.randomUUID());

        // Get raw password from mapped data for hashing
        String rawPassword = newUser.getAuthentication().getPassword();

        // Encrypt raw password
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Set hashed password
        newUser.getAuthentication().setPassword(hashedPassword);

        // Set account's initial status as active
        newUser.setAccountStatus(AccountStatus.ACTIVE);

        User savedUser = userRepository.save(newUser);

        // Return the newly created user
        return userMapper.toUserDto(savedUser);
    }

    /**
     * Updates a user entity from the database.
     *
     * @param userDto User's new details to replace the existing data.
     */
    @Transactional
    @Override
    public void updateUser(UserDto userDto){

        User authenticatedUser = authenticationService.getAuthenticatedUser();

        authenticatedUser.setFirstName(userDto.firstName());
        authenticatedUser.setLastName(userDto.lastName());

        // Save new details
        userRepository.save(authenticatedUser);
    }

    /**
     * Deletes a user from the database
     *
     * @param refId User's reference id
     */
    @PreAuthorize("hasRole('ADMIN')") // Admin is the only one allowed to delete a user account
    @Transactional
    @Override
    public void deleteUser(UUID refId) {
        // Get user to be deleted using reference id
        User user = userRepository.findByReferenceId(refId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with reference id" + refId));

        // Confirm deletion
        userRepository.delete(user);
    }

    /**
     * Deactivates a user by setting account as deactivated(boolean)
     */
    @Transactional
    @Override
    public void deactivateUser() {
        User authenticatedUser = authenticationService.getAuthenticatedUser();

        // Toggle account's status
        AccountStatus newStatus =
                (authenticatedUser.getAccountStatus() == AccountStatus.ACTIVE)?
                AccountStatus.INACTIVE:
                AccountStatus.ACTIVE;

        // Update account status
        authenticatedUser.setAccountStatus(newStatus);

        // Save changes
        userRepository.save(authenticatedUser);
    }
}
