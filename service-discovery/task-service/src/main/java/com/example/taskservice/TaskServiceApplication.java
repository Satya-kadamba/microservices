package com.example.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Task Service - Microservice for task management
 *
 * Features:
 * - Registers with Eureka Discovery Server
 * - Uses Feign Client to communicate with User Service
 * - Validates assignee exists before creating/updating tasks
 *
 * Endpoints:
 * - GET    /api/tasks           - List all tasks
 * - GET    /api/tasks/{id}      - Get task by ID
 * - POST   /api/tasks           - Create new task (validates assignee via user-service)
 * - PUT    /api/tasks/{id}      - Update task
 * - DELETE /api/tasks/{id}      - Delete task
 * - GET    /api/tasks/assignee/{userId} - Get tasks by assignee
 */
@SpringBootApplication
@EnableFeignClients  // Enable Feign clients for inter-service communication
public class TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}
