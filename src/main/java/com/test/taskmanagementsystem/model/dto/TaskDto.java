package com.test.taskmanagementsystem.model.dto;

import com.test.taskmanagementsystem.model.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskDto {

    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String status;
    @NonNull
    private String priority;
    private UserDto author;
    private UserDto assignee;
    private String createdAt;
    private String updatedAt;
}
