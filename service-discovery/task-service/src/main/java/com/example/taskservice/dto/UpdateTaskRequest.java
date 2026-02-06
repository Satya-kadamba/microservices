package com.example.taskservice.dto;

import com.example.taskservice.model.TaskStatus;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
    @Size(max = 200, message = "Title must be less than 200 characters")
    String title,

    @Size(max = 500, message = "Description must be less than 500 characters")
    String description,

    TaskStatus status,

    Long assigneeId
) {}
