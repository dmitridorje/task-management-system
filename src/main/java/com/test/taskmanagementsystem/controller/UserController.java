package com.test.taskmanagementsystem.controller;

import com.test.taskmanagementsystem.model.dto.NewUserDto;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.repository.UserRepository;
import com.test.taskmanagementsystem.service.JwtService;
import com.test.taskmanagementsystem.service.NewUserService;
import com.test.taskmanagementsystem.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private final NewUserService newUserService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getUserTasks(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);

        User user = extractUserFromToken(token);

        String username = jwtService.extractUserName(token);

        List<TaskDto> tasks = taskService.getTasksByUser(user);

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")  // Ограничиваем доступ только для администраторов
    public User addUser(@Valid @RequestBody NewUserDto userDto) {
        return newUserService.createUser(userDto);
    }

    private User extractUserFromToken(String token) {
        String username = jwtService.extractUserName(token);
        User user = userRepository.findByUsername(username).orElse(null);
        return user;
    }
}
