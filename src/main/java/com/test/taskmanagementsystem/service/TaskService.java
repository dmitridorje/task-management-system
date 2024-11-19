package com.test.taskmanagementsystem.service;

import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasksByUser(User user);
    TaskDto createTask(TaskDto taskDto);
}
