/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.mapper;

import com.iamjdribleza.task_management_system.auth.Authentication;
import com.iamjdribleza.task_management_system.auth.AuthenticationRequestDto;
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
    AuthenticationRequestDto toDto(Authentication authentication);

    Authentication toAuth(AuthenticationRequestDto authenticationRequestDto);
    List<AuthenticationRequestDto> toDtoList(List<Authentication> authentications);
}
