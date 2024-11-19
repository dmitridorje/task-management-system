package com.test.taskmanagementsystem.filter;

import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskPriorityFilter implements TaskFilter {

    @Override
    public boolean isApplicable(TaskFilterDto filters) {
        return filters.getPriority() != null;
    }

    @Override
    public Specification<Task> toSpecification(TaskFilterDto filters) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), filters.getPriority());
    }
}
