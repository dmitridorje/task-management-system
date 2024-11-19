package com.test.taskmanagementsystem.controller;

import com.test.taskmanagementsystem.model.dto.CreateUserDto;
import com.test.taskmanagementsystem.model.dto.UserDto;
import com.test.taskmanagementsystem.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Admin users management", description = "Operations for managing users by admin")
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "Add a new user",
            description = "Allows the admin to add a new user to the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(
            @Valid @RequestBody
            @Parameter(description = "Data for creating a new user", required = true) CreateUserDto userDto) {
        return adminService.createUser(userDto);
    }
}
