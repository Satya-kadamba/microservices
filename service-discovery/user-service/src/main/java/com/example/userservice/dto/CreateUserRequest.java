package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
) {}
