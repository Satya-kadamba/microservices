package com.example.taskservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign Client for communicating with User Service.
 *
 * Instead of using RestTemplate or WebClient with hardcoded URLs,
 * Feign provides a declarative way to define HTTP clients.
 *
 * The @FeignClient annotation:
 * - name = "user-service" - the service name registered in Eureka
 * - Eureka resolves this name to the actual URL dynamically
 *
 * Benefits:
 * - No hardcoded URLs
 * - Automatic load balancing if multiple instances exist
 * - Declarative API definition
 * - Automatic serialization/deserialization
 */
@FeignClient(name = "user-service")
public interface UserClient {

    /**
     * Get user by ID from user-service.
     * Maps to: GET http://user-service/api/users/{id}
     */
    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
