package com.test.taskmanagementsystem.filter;

import com.test.taskmanagementsystem.model.dto.TaskFilterDto;
import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskAuthorFilter implements TaskFilter {
    @Override
    public boolean isApplicable(TaskFilterDto filters) {
        return filters.getAuthor() != null && !filters.getAuthor().isEmpty();
    }

    @Override
    public Specification<Task> toSpecification(TaskFilterDto filters) {
        return (root, query, criteriaBuilder) -> {
            if (filters.getAuthor() != null) {
                Join<Task, User> authorJoin = root.join("author", JoinType.INNER);
                return criteriaBuilder.equal(criteriaBuilder.lower(authorJoin.get("username")), filters.getAuthor().toLowerCase());
            }
            return criteriaBuilder.conjunction();
        };
    }
}
