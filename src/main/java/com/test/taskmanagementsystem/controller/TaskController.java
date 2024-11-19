package com.test.taskmanagementsystem.controller;

import com.test.taskmanagementsystem.model.CommentDto;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.repository.UserRepository;
import com.test.taskmanagementsystem.service.JwtService;
import com.test.taskmanagementsystem.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PostMapping("/{taskId}/comments")
    public TaskDto addComment(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CommentDto commentDto, @PathVariable Long taskId) {
        String token = authorizationHeader.substring(7);

        User user = extractUserFromToken(token);
        return taskService.addComment(commentDto, taskId);
    }

    private User extractUserFromToken(String token) {
        String username = jwtService.extractUserName(token);
        User user = userRepository.findByUsername(username).orElse(null);
        return user;
    }


    @PostMapping("/filtered")
    public List<TaskDto> getFilteredTasks(@RequestBody @Valid TaskFilterDto taskFilterDto) {
        log.info("Received filter: {}", taskFilterDto);
        return taskService.getFilteredTasks(taskFilterDto);
    }

}
