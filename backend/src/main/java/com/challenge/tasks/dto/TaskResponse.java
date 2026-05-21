package com.challenge.tasks.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.challenge.tasks.model.TaskStatus;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        OffsetDateTime dueDate,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
