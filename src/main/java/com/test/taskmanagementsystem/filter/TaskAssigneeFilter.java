package com.test.taskmanagementsystem.filter;

import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskAssigneeFilter implements TaskFilter {
    @Override
    public boolean isApplicable(TaskFilterDto filters) {
        return filters.getAssignee() != null && !filters.getAssignee().isEmpty();
    }

    @Override
    public Specification<Task> toSpecification(TaskFilterDto filters) {
        return (root, query, criteriaBuilder) -> {
            if (filters.getAssignee() != null) {
                // Создаем join с таблицей пользователей
                Join<Task, User> assigneeJoin = root.join("assignee", JoinType.INNER);
                // Добавляем условие для фильтрации по имени пользователя
                return criteriaBuilder.equal(criteriaBuilder.lower(assigneeJoin.get("username")), filters.getAssignee().toLowerCase());
            }
            return criteriaBuilder.conjunction(); // Если фильтр по автору не задан, возвращаем пустое условие
        };
    }
}
