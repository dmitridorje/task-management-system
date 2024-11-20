package com.test.taskmanagementsystem.service.impl;

import com.test.taskmanagementsystem.exception.UnauthorizedAccessException;
import com.test.taskmanagementsystem.filter.TaskFilter;
import com.test.taskmanagementsystem.mapper.TaskMapper;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.dto.requestdtos.ChangeStatusDto;
import com.test.taskmanagementsystem.model.dto.requestdtos.CreateCommentDto;
import com.test.taskmanagementsystem.model.entity.Comment;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.model.enums.TaskPriority;
import com.test.taskmanagementsystem.model.enums.TaskStatus;
import com.test.taskmanagementsystem.repository.TaskRepository;
import com.test.taskmanagementsystem.repository.UserRepository;
import com.test.taskmanagementsystem.service.TaskService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final List<TaskFilter> taskFilters;

    @Override
    public List<TaskDto> getTasksByUser(User currentUser, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Task> tasks = taskRepository.findByAssignee(currentUser, pageable);

        return taskMapper.toTaskDtoListFromPage(tasks);
    }

    @Override
    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.valueOf(taskDto.getStatus().toUpperCase()));
        task.setPriority(TaskPriority.valueOf(taskDto.getPriority().toUpperCase()));
        task.setAuthor(userRepository.findIdByFirstName(taskDto.getAuthor().getFirstName()));
        task.setAssignee(userRepository.findIdByFirstName(taskDto.getAssignee().getFirstName()));

        Task savedTask = taskRepository.save(task);

        return taskMapper.toTaskDto(savedTask);
    }

    @Override
    @Transactional
    public TaskDto addComment(Long taskId, CreateCommentDto commentDto, User currentUser) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));


        if ("USER".equals(currentUser.getRole().name()) && !task.getAssignee().equals(currentUser)) {
            throw new UnauthorizedAccessException("You can only comment on tasks assigned to you.");
        }

        Comment comment = new Comment(commentDto.getContent(), currentUser, task);
        task.getComments().add(comment);
        taskRepository.save(task);

        return taskMapper.toTaskDto(task);
    }

    @Override
    public List<TaskDto> getFilteredTasks(TaskFilterDto taskFilterDto) {

        Page<Task> filteredEvents = getFilteredTasksFromRepository(taskFilterDto);

        return taskMapper.toTaskDtoListFromPage(filteredEvents);
    }

    @Override
    public TaskDto updateTask(Long taskId, TaskDto requestDto) {
        Task task = taskRepository.findByIdWithComments(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setStatus(TaskStatus.valueOf(requestDto.getStatus().toUpperCase()));
        task.setPriority(TaskPriority.valueOf(requestDto.getPriority().toUpperCase()));
        task.setAuthor(userRepository.findIdByFirstName(requestDto.getAuthor().getFirstName()));
        task.setAssignee(userRepository.findIdByFirstName(requestDto.getAssignee().getFirstName()));

        taskRepository.save(task);

        return taskMapper.toTaskDto(task);
    }

    public TaskDto changeTaskStatus(Long taskId, ChangeStatusDto statusChangeRequestDto, User currentUser) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        if ("USER".equals(currentUser.getRole().name()) && !task.getAssignee().equals(currentUser)) {
            throw new UnauthorizedAccessException("You can only change status on tasks assigned to you.");
        }

        task.setStatus(TaskStatus.valueOf(statusChangeRequestDto.getStatus().toUpperCase()));
        taskRepository.save(task);

        return taskMapper.toTaskDto(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.deleteById(taskId);
        } else {
            throw new EntityNotFoundException("Task not found with ID: " + taskId);
        }
    }

    private Page<Task> getFilteredTasksFromRepository(TaskFilterDto taskFilterDto) {
        Pageable pageable = PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getPageSize());

        Specification<Task> specification = Specification.where(null);

        for (TaskFilter filter : taskFilters) {
            if (filter.isApplicable(taskFilterDto)) {
                specification = specification.and(filter.toSpecification(taskFilterDto));
            }
        }

        return taskRepository.findAll(specification, pageable);
    }
}
