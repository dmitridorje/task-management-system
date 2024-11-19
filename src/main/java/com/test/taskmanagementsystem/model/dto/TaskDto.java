package com.test.taskmanagementsystem.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
public class TaskDto {

    private Long id;
    @NotNull(message = "Field 'title' cannot be null.")
    private String title;
    @NotNull(message = "Field 'description' cannot be null.")
    private String description;
    @NotNull(message = "Field 'status' cannot be null.")
    private String status;
    @NotNull(message = "Field 'priority' cannot be null.")
    private String priority;
    private UserDto author;
    private UserDto assignee;
    List<CommentDto> comments;
    private String createdAt;
    private String updatedAt;
}
