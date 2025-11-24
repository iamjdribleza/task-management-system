/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.task;


import com.iamjdribleza.task_management_system.enums.Priority;
import com.iamjdribleza.task_management_system.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for Task.
 *
 * @author iamjdribleza
 * @version 1.0
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@Entity
public class Task {

    @Id
    @SequenceGenerator(
            name = "task_seq_gen",
            sequenceName = "task_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_seq_gen"
    )
    private long id;


    // Used to expose task's identity to the client
    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private UUID referenceId;


    @ManyToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name ="user_id",
            referencedColumnName = "id"
    )
    private User user;


    @Column(
            nullable = false,
            columnDefinition = "text"
    )
    private String description;


    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private Priority priority;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(updatable = false)
    @UpdateTimestamp
    private LocalDateTime dateUpdated;
}