/*
 * Copyright (c) 2025.
 * Jann Dervin Ribleza.
 * All Right Reserved.
 */

package com.iamjdribleza.task_management_system.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date formatting
 *
 * @author iamjdribleza
 * @version 1.0
 */

@UtilityClass
public class DateUtil {
    public static String format(LocalDateTime date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy E @ h:mma");
        return dateTimeFormatter.format(date);
    }
}
