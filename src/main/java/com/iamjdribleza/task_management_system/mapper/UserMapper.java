/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.mapper;

import com.iamjdribleza.task_management_system.user.User;
import com.iamjdribleza.task_management_system.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "auth.password", ignore = true)
    UserDto toUserDto(User user);

    @Mapping(target = "roles", ignore = true)
    User toUser(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);
}
