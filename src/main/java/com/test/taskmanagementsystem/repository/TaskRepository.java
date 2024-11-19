package com.test.taskmanagementsystem.repository;

import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByAssignee(User assignee);


    // Метод findAll с аннотациями @NonNull
    @NonNull
    Page<Task> findAll(Specification<Task> specification, @NonNull Pageable pageable);
}
