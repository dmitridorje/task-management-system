package com.test.taskmanagementsystem.service.impl;

import com.test.taskmanagementsystem.filter.TaskFilter;
import com.test.taskmanagementsystem.mapper.TaskMapper;
import com.test.taskmanagementsystem.model.CommentDto;
import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.model.enums.TaskPriority;
import com.test.taskmanagementsystem.model.enums.TaskStatus;
import com.test.taskmanagementsystem.repository.TaskRepository;
import com.test.taskmanagementsystem.repository.UserRepository;
import com.test.taskmanagementsystem.service.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
//@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final List<TaskFilter> taskFilters;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           UserRepository userRepository,
                           List<TaskFilter> taskFilters) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
        this.taskFilters = taskFilters;

        log.info("TaskServiceImpl bean created with dependencies: TaskRepository={}, TaskMapper={}, UserRepository={}, TaskFilters={}",
                taskRepository != null,
                taskMapper != null,
                userRepository != null,
                taskFilters != null ? taskFilters.size() : 0);
    }

    @Override
    @Cacheable(value = "tasks", key = "#user.id")
    public List<TaskDto> getTasksByUser(User user) {
        log.info("Attempting to fetch tasks for user with ID: {}", user.getId());

        // Логируем, если кэш пуст и данные будут извлечены из базы
        List<Task> tasks = taskRepository.findByAssignee(user);

        // Логируем, что данные извлекаются из базы
        log.info("Fetched {} tasks for user with ID: {}", tasks.size(), user.getId());

        // Маппим задачи в TaskDto
        List<TaskDto> taskDtos = taskMapper.toTaskDtoList(tasks);

        // Возвращаем результат
        return taskDtos;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.valueOf(taskDto.getStatus().toUpperCase())); // Конвертация строки в Enum
        task.setPriority(TaskPriority.valueOf(taskDto.getPriority().toUpperCase())); // Аналогично
        task.setAuthor(userRepository.findIdByFirstName(taskDto.getAuthor().getFirstName()));
        task.setAssignee(userRepository.findIdByFirstName(taskDto.getAssignee().getFirstName()));
        return taskMapper.toTaskDto(taskRepository.save(task));
    }

    @Override
    public TaskDto addComment(CommentDto commentDto, Long taskId) {
        return null;
    }

    @Override
    public List<TaskDto> getFilteredTasks(TaskFilterDto taskFilterDto) {

        Page<Task> filteredEvents = getFilteredTasksFromRepository(taskFilterDto);



        return taskMapper.toTaskDtoListFromPage(filteredEvents);
    }

//    private Page<Task> getFilteredTasksFromRepository(TaskFilterDto taskFilterDto) {
//        // Создаем Pageable объект на основе параметров фильтра
//        Pageable pageable = PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getPageSize());
//
//        Specification<Task> spec = taskFilters.stream()
//                .filter(filter -> filter.isApplicable(taskFilterDto))
//                .map(filter -> filter.toSpecification(taskFilterDto))
//                .reduce(Specification::and)
//                .orElse(null);
//        return taskRepository.findAll(spec, pageable);
//    }

    private Page<Task> getFilteredTasksFromRepository(TaskFilterDto taskFilterDto) {
        // Создаем Pageable объект на основе параметров фильтра
        Pageable pageable = PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getPageSize());

        // Собираем спецификации для всех фильтров
        Specification<Task> specification = Specification.where(null);

        for (TaskFilter filter : taskFilters) {
            if (filter.isApplicable(taskFilterDto)) {
                specification = specification.and(filter.toSpecification(taskFilterDto));
            }
        }

        // Выполняем запрос с пагинацией и фильтрацией
        return taskRepository.findAll(specification, pageable);
    }
}
