package com.test.taskmanagementsystem.model.dto.requestdtos;

import com.test.taskmanagementsystem.model.dto.UserDto;
import com.test.taskmanagementsystem.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserDto extends UserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Role role;
}
