/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.user;

import com.iamjdribleza.task_management_system.auth.Authentication;
import com.iamjdribleza.task_management_system.enums.AccountStatus;
import com.iamjdribleza.task_management_system.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity for User.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_seq_gen",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq_gen"
    )
    private long id;

    // Used to expose user's identity to the client
    @Column(
            unique = true,
            updatable = false,
            nullable = false,
            columnDefinition = "UUID"
    )
    private UUID referenceId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;


    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name ="auth_id",
            referencedColumnName = "id"
    )
    private Authentication authentication;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @OneToMany(
            cascade = CascadeType.REMOVE,
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    private List<Task> tasks;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @CreationTimestamp
    private LocalDate dateCreated;

    @UpdateTimestamp
    private LocalDate dateUpdated;
}
