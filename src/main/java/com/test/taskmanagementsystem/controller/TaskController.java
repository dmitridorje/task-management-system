package com.test.taskmanagementsystem.controller;

import com.test.taskmanagementsystem.model.dto.CommentDto;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.repository.UserRepository;
import com.test.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tasks", description = "Operations related to task management")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @Operation(summary = "Get filtered tasks", description = "Fetch tasks based on filters. Accessible only to admin users.")
    @PostMapping("/filtered")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<TaskDto> getFilteredTasks(@RequestBody @Valid @Parameter(description = "Filters for fetching tasks") TaskFilterDto taskFilterDto) {
        return taskService.getFilteredTasks(taskFilterDto);
    }

    @Operation(summary = "Get user's tasks", description = "Fetch tasks for the authenticated user. Accessible only to regular users.")
    @GetMapping()
    @PreAuthorize("hasAuthority('USER')")
    public List<TaskDto> getUserTasks(
            @Valid @RequestParam @Min(value = 0, message = "Page index must be greater than or equal to 0")
            @Parameter(description = "Page number, starting from 0") int page,
            @Valid @RequestParam @Min(value = 1, message = "Page size must be greater than or equal to 1")
            @Parameter(description = "Number of items per page") int pageSize,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        return taskService.getTasksByUser(currentUser, page, pageSize);
    }

    @Operation(summary = "Create a new task", description = "Create a new task. Accessible only to admin users.")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public TaskDto createTask(
            @RequestBody @Valid @Parameter(description = "Task data for the new task") TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @Operation(summary = "Add a comment to a task", description = "Add a comment to a specific task. Accessible to admin and user roles.")
    @PostMapping("/{taskId}/comments")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TaskDto addComment(
            @PathVariable @Parameter(description = "ID of the task to add a comment to") Long taskId,
            @RequestBody @Parameter(description = "Data for the new comment") CommentDto commentDto,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication); // Получаем текущего пользователя
        TaskDto updatedTask = taskService.addComment(taskId, commentDto, currentUser);
        return updatedTask;
    }

    @Operation(summary = "Update a task", description = "Update details of a specific task. Accessible only to admin users.")
    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public TaskDto updateTask(
            @PathVariable @Parameter(description = "ID of the task to update") Long taskId,
            @Valid @RequestBody @Parameter(description = "Updated task data") TaskDto requestDto) {
        return taskService.updateTask(taskId, requestDto);
    }

    @Operation(summary = "Change task status", description = "Change the status of a specific task. Accessible only to regular users.")
    @PatchMapping("/{taskId}/status")
    @PreAuthorize("hasAuthority('USER')")
    public TaskDto changeTaskStatus(
            @PathVariable @Parameter(description = "ID of the task to change status") Long taskId,
            @RequestBody @Parameter(description = "Data for status change") TaskFilterDto statusChangeRequestDto,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        return taskService.changeTaskStatus(taskId, statusChangeRequestDto, currentUser);
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
