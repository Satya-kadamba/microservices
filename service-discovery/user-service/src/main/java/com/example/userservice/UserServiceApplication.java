package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User Service - Microservice for user management
 *
 * This service registers itself with Eureka Discovery Server,
 * allowing other services to discover and communicate with it
 * using the service name instead of hardcoded URLs.
 *
 * Endpoints:
 * - GET    /api/users      - List all users
 * - GET    /api/users/{id} - Get user by ID
 * - POST   /api/users      - Create new user
 * - PUT    /api/users/{id} - Update user
 * - DELETE /api/users/{id} - Delete user
 */
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
