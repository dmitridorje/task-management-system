package com.test.taskmanagementsystem.repository;

import com.test.taskmanagementsystem.model.entity.Task;
import com.test.taskmanagementsystem.model.entity.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @NonNull
    Page<Task> findByAssignee(User assignee, @NonNull Pageable pageable);

    @NonNull
    Page<Task> findAll(Specification<Task> specification, @NonNull Pageable pageable);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.comments WHERE t.id = :taskId")
    Optional<Task> findByIdWithComments(@Param("taskId") Long taskId);
}
