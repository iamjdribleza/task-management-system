/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.user;

import com.iamjdribleza.task_management_system.auth.AuthService;
import com.iamjdribleza.task_management_system.exceptions.ResourceAlreadyExists;
import com.iamjdribleza.task_management_system.exceptions.ResourceNotFoundException;
import com.iamjdribleza.task_management_system.mapper.UserMapper;
import com.iamjdribleza.task_management_system.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final AuthService authService;

    /**
     * Retrieves a user using reference id from the database.
     *
     * @param refId User's reference id.
     * @return User's details.
     * @throws ResourceNotFoundException if user is not found.
     */
    @Override
    public UserDto getUser(UUID refId) {
        return userRepository.findByRefId(refId)
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
        if (userRepository.existsByAuthEmail(userDto.auth().email()))
            // Throw exception
            throw new ResourceAlreadyExists("email");

        // If email doesn't exist, then create a new instance of User
        User newUser = userMapper.toUser(userDto);

        // Encrypt and set Account password
        String rawPassword = newUser.getAuth().getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        newUser.getAuth().setPassword(hashedPassword);

        // Initialize role as regular user
        GrantedAuthority initialRole = new SimpleGrantedAuthority(Role.USER.name());

        // Set roles - USER for all users
        newUser.setRoles(List.of(initialRole));

        User savedUser = userRepository.save(newUser);

        // Return the newly created user
        return userMapper.toUserDto(savedUser);
    }

    /**
     * Updates a user from the database.
     *
     * @param userDto User's new details to replace the existing data.
     */
    @Transactional
    @Override
    public void updateUser(UserDto userDto){

        User authenticatedUser = authService.getAuthenticatedUser();

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
    @Transactional
    @Override
    public void deleteUser(UUID refId) {
        userRepository.findByRefId(refId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with reference id" + refId));
    }
}
