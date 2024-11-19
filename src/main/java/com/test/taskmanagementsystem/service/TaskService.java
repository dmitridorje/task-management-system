package com.test.taskmanagementsystem.service;

import com.test.taskmanagementsystem.model.dto.CommentDto;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.User;


import java.util.List;

public interface TaskService {

    List<TaskDto> getTasksByUser(User currentUser, int page, int pageSize);
    TaskDto createTask(TaskDto taskDto);
    TaskDto addComment(Long taskId, CommentDto commentDto, User currentUser);
    List<TaskDto> getFilteredTasks(TaskFilterDto taskFilterDto);

    TaskDto updateTask(Long taskId, TaskDto requestDto);

    TaskDto changeTaskStatus(Long taskId, TaskFilterDto statusChangeRequestDto, User currentUser);
}
