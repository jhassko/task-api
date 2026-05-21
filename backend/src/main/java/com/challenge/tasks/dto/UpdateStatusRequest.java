package com.challenge.tasks.dto;

import com.challenge.tasks.model.TaskStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "Status is required") TaskStatus status
) {}
