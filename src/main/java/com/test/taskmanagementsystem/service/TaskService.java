package com.test.taskmanagementsystem.service;

import com.test.taskmanagementsystem.model.CommentDto;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasksByUser(User user);
    TaskDto createTask(TaskDto taskDto);
    TaskDto addComment(CommentDto commentDto, Long taskId);

    List<TaskDto> getFilteredTasks(TaskFilterDto taskFilterDto);
}
