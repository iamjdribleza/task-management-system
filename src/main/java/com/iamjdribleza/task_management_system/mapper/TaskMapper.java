/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.mapper;

import com.iamjdribleza.task_management_system.task.TaskDto;
import com.iamjdribleza.task_management_system.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Mapper class for mapping task details from entity to dto and vice versa
 *
 * @author iamjdribleza
 * @version 1.0
 */

@Mapper(componentModel =  "spring")
public interface TaskMapper {

    /**
     * Maps a task to dto
     *
     * @param task Task from database to be mapped
     * @return Task's details
     */
    @Mapping(target = "timeAgo", expression = "java(formatTimeAgo(task.getDateCreated()))")
    @Mapping(target = "eventDate", expression = "java(formatDate(task.getEventDate()))")
    TaskDto toDto(Task task);

    /**
     * Maps dto to task
     *
     * @param taskDto Task details from client to be mapped.
     * @return Task's details
     */
    Task toTask(TaskDto taskDto);

    /**
     * Maps a list of tasks to a list of dto.
     *
     * @param tasks List of tasks to be mapped
     * @return List of tasks
     */
    List<TaskDto> toDtoList(List<Task> tasks);

    /**
     * Formats a date + time to time ago
     *
     * @param dateTimePosted Date and time the task was created.
     * @return Date or time task was created. e.g "20 minutes ago"
     */
    default String formatTimeAgo(LocalDateTime dateTimePosted){
        LocalDateTime today = LocalDateTime.now();

        if (dateTimePosted == null)
            return "no dateTimeposted";

        long seconds = ChronoUnit.SECONDS.between(dateTimePosted, today);
        if (seconds < 60) return seconds + " seconds ago";

        long minutes = ChronoUnit.MINUTES.between(dateTimePosted, today);
        if (minutes < 60) return minutes + " minutes ago";

        long hours = ChronoUnit.HOURS.between(dateTimePosted, today);
        if (hours < 24) return hours + (hours == 1? " hour ago": " hours ago");

        long days = ChronoUnit.DAYS.between(dateTimePosted, today);
        if (days < 30) return days + (days == 1? " day ago": " days ago");

        long months = ChronoUnit.MONTHS.between(dateTimePosted, today);

        return months + (months == 1? " month ago": "months ago");
    }

    /**
     * Formats a date
     *
     * @param due Date when task's occurs
     * @return Date task's will occur
     */
    default String formatDate(LocalDate due){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd");

        return dateTimeFormatter.format(due);
    }
}
