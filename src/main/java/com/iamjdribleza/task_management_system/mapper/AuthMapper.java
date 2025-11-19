/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.mapper;

import com.iamjdribleza.task_management_system.auth.Auth;
import com.iamjdribleza.task_management_system.auth.AuthDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper class for mapping Auth details to dto and vice versa
 *
 * @author iamjdribleza
 * @version 1.0
 */

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "password", ignore = true)
    AuthDto toDto(Auth auth);

    Auth toAuth(AuthDto authDto);
    List<AuthDto> toDtoList(List<Auth> auths);
}
