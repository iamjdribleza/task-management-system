/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.user;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 *
 * @author iamjdribleza
 * @version 1.0
 */
public interface UserService {
    UserDto getUser(UUID refId);
    List<UserDto> getUsers();
    UserDto createUser(UserDto userDto);
    void updateUser(UserDto userDto);
    void deleteUser(UUID refId);
    void deactivateUser();
}
