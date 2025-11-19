/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.auth;

import com.iamjdribleza.task_management_system.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entity for Authentication.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@Entity
public class Auth {
    @Id
    @SequenceGenerator(
            name = "auth_seq_gen",
            sequenceName = "auth_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "auth_seq_gen"
    )
    private long id;

    @Column(
            unique = true,
            updatable = false,
            nullable = false,
            columnDefinition = "UUID"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID referenceId;

    @Column(
            nullable = false,
            updatable = false,
            unique = true
    )
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "auth")
    private User user;
}
