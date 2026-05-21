package com.challenge.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

import com.challenge.tasks.model.TaskStatus;

public record UpdateTaskRequest(
        @NotBlank(message = "Title is required") String title,
        String description,
        @NotNull(message = "Status is required") TaskStatus status,
        @NotNull(message = "Due date is required") OffsetDateTime dueDate
) {}
