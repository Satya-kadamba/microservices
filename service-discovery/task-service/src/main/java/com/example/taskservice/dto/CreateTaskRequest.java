package com.example.taskservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    String title,

    @Size(max = 500, message = "Description must be less than 500 characters")
    String description,

    Long assigneeId  // Optional - validated against user-service if provided
) {}
