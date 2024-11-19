package com.test.taskmanagementsystem.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
