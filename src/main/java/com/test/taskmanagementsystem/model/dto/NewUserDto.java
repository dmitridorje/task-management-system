package com.test.taskmanagementsystem.model.dto;

import com.test.taskmanagementsystem.model.enums.Role;
import lombok.Data;

@Data
public class NewUserDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Role role;
}
