package com.example.taskservice.client;

import java.time.LocalDateTime;

/**
 * DTO representing user data received from User Service.
 * This mirrors the UserResponse from user-service.
 */
public record UserDTO(
    Long id,
    String name,
    String email,
    LocalDateTime createdAt
) {}
