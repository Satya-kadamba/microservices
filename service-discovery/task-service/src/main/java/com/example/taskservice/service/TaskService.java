package com.example.taskservice.service;

import com.example.taskservice.client.UserClient;
import com.example.taskservice.client.UserDTO;
import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.dto.UpdateTaskRequest;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.exception.UserNotFoundException;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final UserClient userClient;

    public TaskService(TaskRepository taskRepository, UserClient userClient) {
        this.taskRepository = taskRepository;
        this.userClient = userClient;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        log.info("Creating task: {}", request.title());

        // Validate assignee exists in user-service
        UserDTO assignee = null;
        if (request.assigneeId() != null) {
            assignee = validateAndGetUser(request.assigneeId());
        }

        Task task = new Task(
            request.title(),
            request.description(),
            request.assigneeId()
        );

        Task saved = taskRepository.save(task);
        log.info("Task created with id: {}", saved.getId());

        return TaskResponse.fromEntityWithUser(saved, assignee);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> enrichTaskWithUser(task))
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return enrichTaskWithUser(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByAssignee(Long userId) {
        // First validate user exists
        validateAndGetUser(userId);

        return taskRepository.findByAssigneeId(userId).stream()
                .map(task -> enrichTaskWithUser(task))
                .toList();
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Validate new assignee if provided
        UserDTO assignee = null;
        if (request.assigneeId() != null) {
            assignee = validateAndGetUser(request.assigneeId());
            task.setAssigneeId(request.assigneeId());
        }

        if (request.title() != null && !request.title().isBlank()) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }

        Task updated = taskRepository.save(task);
        log.info("Task {} updated", id);

        if (assignee == null && task.getAssigneeId() != null) {
            assignee = getUserSafely(task.getAssigneeId());
        }

        return TaskResponse.fromEntityWithUser(updated, assignee);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
        log.info("Task {} deleted", id);
    }

    /**
     * Validates user exists in user-service via Feign client.
     * Throws UserNotFoundException if user doesn't exist.
     */
    private UserDTO validateAndGetUser(Long userId) {
        try {
            log.debug("Validating user {} exists in user-service", userId);
            return userClient.getUserById(userId);
        } catch (FeignException.NotFound e) {
            log.warn("User {} not found in user-service", userId);
            throw new UserNotFoundException(userId);
        } catch (FeignException e) {
            log.error("Error communicating with user-service: {}", e.getMessage());
            throw new RuntimeException("Unable to validate user. User service may be unavailable.");
        }
    }

    /**
     * Gets user info without failing if user not found.
     * Used for enriching responses where missing user is acceptable.
     */
    private UserDTO getUserSafely(Long userId) {
        try {
            return userClient.getUserById(userId);
        } catch (FeignException e) {
            log.debug("Could not fetch user {}: {}", userId, e.getMessage());
            return null;
        }
    }

    /**
     * Enriches task response with user information from user-service.
     */
    private TaskResponse enrichTaskWithUser(Task task) {
        UserDTO user = null;
        if (task.getAssigneeId() != null) {
            user = getUserSafely(task.getAssigneeId());
        }
        return TaskResponse.fromEntityWithUser(task, user);
    }
}
