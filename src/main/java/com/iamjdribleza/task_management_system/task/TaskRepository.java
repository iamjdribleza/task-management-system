/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.task;

import com.iamjdribleza.task_management_system.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Task Repository
 *
 * @author iamjdribleza
 * @version 1.0
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByRefId(UUID refId);
    Page<Task> findByUserAndEventDate(User user, Pageable pageable, LocalDate today);
}