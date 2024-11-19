package com.test.taskmanagementsystem.filter;

import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskTitleFilter implements TaskFilter {

    @Override
    public boolean isApplicable(TaskFilterDto filters) {
        boolean applicable = filters.getTitlePattern() != null;
        log.info("Is titlePattern applicable? {}", applicable);
        return filters.getTitlePattern() != null;
    }

    @Override
    public Specification<Task> toSpecification(TaskFilterDto filters) {
        log.info("Filtering by title pattern: {}", filters.getTitlePattern());  // Логируем значение
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + filters.getTitlePattern().toLowerCase() + "%"
                );
    }
}
