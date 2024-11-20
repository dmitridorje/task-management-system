package com.test.taskmanagementsystem.repository;

import com.test.taskmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    User findIdByFirstName(String firstName);

    Optional<User> getUserById(Long userId);
}