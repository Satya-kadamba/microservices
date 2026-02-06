package com.example.taskservice.dto;

import com.example.taskservice.client.UserDTO;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status,
    Long assigneeId,
    String assigneeName,  // Enriched from user-service
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getAssigneeId(),
            null,  // No assignee name without user lookup
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }

    public static TaskResponse fromEntityWithUser(Task task, UserDTO user) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getAssigneeId(),
            user != null ? user.name() : null,
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }
}
