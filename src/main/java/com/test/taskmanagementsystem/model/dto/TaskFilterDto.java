package com.test.taskmanagementsystem.model.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskFilterDto {

    private String titlePattern;

    @Pattern(regexp = "^(PENDING|IN_PROGRESS|COMPLETED)$", message = "Status must be one of PENDING, IN_PROGRESS, COMPLETED")
    private String status;

    @Pattern(regexp = "^(LOW|MEDIUM|HIGH)$", message = "Priority must be one of LOW, MEDIUM, HIGH")
    private String priority;

    private String author;

    private String assignee;

    @Min(value = 0, message = "page must be a positive number")
    private int page;

    @Min(value = 1, message = "pageSize must be at least 1")
    private int pageSize;
}
