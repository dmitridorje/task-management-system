package com.test.taskmanagementsystem.mapper;

import com.test.taskmanagementsystem.model.dto.TaskDto;
import com.test.taskmanagementsystem.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "formatDate")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "formatDate")
    TaskDto toTaskDto(Task task);

    Task toTaskEntity(TaskDto taskDto);

    List<TaskDto> toTaskDtoList(List<Task> taskList);

    default List<TaskDto> toTaskDtoListFromPage(Page<Task> tasks) {
        return tasks.stream()
                .map(this::toTaskDto)  // Преобразуем каждую задачу
                .collect(Collectors.toList());
    }

    @Named("formatDate")
    static String formatDate(LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return dateTime.format(formatter);
        }
        return null;
    }
}
