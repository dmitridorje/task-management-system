package com.test.taskmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.taskmanagementsystem.model.dto.CommentDto;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.dto.UserDto;
import com.test.taskmanagementsystem.model.dto.jwt.request.SignInRequest;
import com.test.taskmanagementsystem.model.dto.jwt.response.JwtAuthenticationResponse;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.enums.TaskPriority;
import com.test.taskmanagementsystem.model.enums.TaskStatus;
import com.test.taskmanagementsystem.repository.TaskRepository;
import com.test.taskmanagementsystem.util.ContainerCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
public class TaskControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TaskRepository taskRepository;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = ContainerCreator.POSTGRES_CONTAINER;

    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void overrideSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
        registry.add("spring.liquibase.enabled", () -> false);
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return filtered tasks when accessed by ADMIN")
    void testGetFilteredTasks_Success() throws Exception {
        TaskFilterDto taskFilterDto = new TaskFilterDto();
        taskFilterDto.setPageSize(10);
        taskFilterDto.setPage(0);
        taskFilterDto.setTitlePattern("Task");
        taskFilterDto.setStatus("PENDING");
        taskFilterDto.setPriority("HIGH");

        String filterJson = objectMapper.writeValueAsString(taskFilterDto);

        String adminToken = "Bearer " + getAdminToken();

        mockMvc.perform(post("/api/v1/tasks/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterJson)
                        .header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[1].title").value("Task 3"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }

    @Test
    @DisplayName("Should return tasks for the current user with pagination")
    void testGetUserTasks_Success() throws Exception {
        int page = 0;
        int pageSize = 2;

        String userToken = "Bearer " + getUserToken();

        mockMvc.perform(get("/api/v1/tasks")
                        .param("page", valueOf(page))
                        .param("pageSize", valueOf(pageSize))
                        .header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(pageSize))
                .andExpect(jsonPath("$[0].author.firstName").value("admin"))
                .andExpect(jsonPath("$[0].assignee.firstName").value("user"));
    }

    @Test
    @DisplayName("Should create a new task when accessed by ADMIN")
    void testCreateTask_Success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("user");
        userDto.setLastName("userovich");

        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New Task");
        taskDto.setDescription("Description of the new task");
        taskDto.setStatus(String.valueOf(TaskStatus.PENDING));
        taskDto.setPriority(String.valueOf(TaskPriority.HIGH));
        taskDto.setAuthor(userDto);
        taskDto.setAssignee(userDto);
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        String adminToken = "Bearer " + getAdminToken();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson)
                        .header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @DisplayName("Should add comment to a task when accessed by authorized user")
    void testAddComment_Success() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("This is a new comment");
        String commentJson = objectMapper.writeValueAsString(commentDto);

        Long taskId = 1L;

        String userToken = "Bearer " + getUserToken();

        mockMvc.perform(post("/api/v1/tasks/{taskId}/comments", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson)
                        .header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.comments.length()").value(1))
                .andExpect(jsonPath("$.comments[0].content").value("This is a new comment"));
    }

    @Test
    @DisplayName("Should return validation error for invalid task data")
    void testCreateTask_InvalidData() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription("Invalid task without title");
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        String adminToken = "Bearer " + getUserToken();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson)
                        .header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Field 'title' cannot be null."));
    }

    @Test
    @DisplayName("Should deny access to create task for user without ADMIN privileges")
    void testCreateTask_AccessDenied() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("user");
        userDto.setLastName("userovich");

        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New Task");
        taskDto.setDescription("Description of the new task");
        taskDto.setStatus(String.valueOf(TaskStatus.PENDING));
        taskDto.setPriority(String.valueOf(TaskPriority.HIGH));
        taskDto.setAuthor(userDto);
        taskDto.setAssignee(userDto);
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        String userToken = "Bearer " + getUserToken();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson)
                        .header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Access Denied"))
                .andExpect(jsonPath("$.message").value("You do not have permission to perform this action."));
    }

    @Test
    @DisplayName("Should allow a regular user to change the status of their task")
    void testChangeTaskStatus_Success() throws Exception {
        Long taskId = 1L;
        TaskFilterDto statusChangeRequestDto = new TaskFilterDto();
        statusChangeRequestDto.setStatus("IN_PROGRESS");
        String requestJson = objectMapper.writeValueAsString(statusChangeRequestDto);

        String userToken = "Bearer " + getUserToken();

        mockMvc.perform(patch("/api/v1/tasks/{taskId}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Should not allow a user to change the status of a task assigned to another user")
    void testChangeTaskStatus_Unauthorized() throws Exception {
        Long taskId = 2L;
        TaskFilterDto statusChangeRequestDto = new TaskFilterDto();
        statusChangeRequestDto.setStatus("IN_PROGRESS");
        String requestJson = objectMapper.writeValueAsString(statusChangeRequestDto);

        String userToken = "Bearer " + getUserToken();

        mockMvc.perform(patch("/api/v1/tasks/{taskId}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Unauthorized Access"))
                .andExpect(jsonPath("$.message").value("You can only change status on tasks assigned to you."));
    }

    @Test
    @DisplayName("Should allow admin to delete a task")
    void testDeleteTask_Success() throws Exception {
        Long taskId = 1L;

        String adminToken = "Bearer " + getAdminToken();

        mockMvc.perform(delete("/api/v1/tasks/{taskId}", taskId)
                        .header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isNoContent());

        Optional<Task> task = taskRepository.findById(taskId);
        assertFalse(task.isPresent(), "Task should be deleted from the database");
    }

    @Test
    @DisplayName("Should return not found when deleting a non-existing task")
    void testDeleteTask_EntityNotFound() throws Exception {
        Long taskId = 999L;

        String adminToken = "Bearer " + getAdminToken();

        mockMvc.perform(delete("/api/v1/tasks/{taskId}", taskId)
                        .header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Entity Not Found"))
                .andExpect(jsonPath("$.message").value("Task not found with ID: " + taskId));
    }

    private String getAdminToken() throws Exception {
        SignInRequest signInRequest = new SignInRequest("admin@example.com", "admin");
        String requestJson = objectMapper.writeValueAsString(signInRequest);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        JwtAuthenticationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), JwtAuthenticationResponse.class);

        return response.getToken();
    }

    private String getUserToken() throws Exception {
        SignInRequest signInRequest = new SignInRequest("user@example.com", "password");
        String requestJson = objectMapper.writeValueAsString(signInRequest);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        JwtAuthenticationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), JwtAuthenticationResponse.class);

        return response.getToken();
    }
}
