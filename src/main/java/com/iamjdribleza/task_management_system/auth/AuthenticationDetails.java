/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import com.iamjdribleza.task_management_system.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Class implementation of UserDetails for security and authentication purposes.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@Getter
@RequiredArgsConstructor

public class AuthenticationDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return this.user.getAuthentication().getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getAuthentication().getEmail();
    }

}
