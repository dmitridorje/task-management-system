package com.test.taskmanagementsystem.config.database;

import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.model.enums.Role;
import com.test.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {
    private final UserRepository userRepository;


    @Bean
    public CommandLineRunner init() {
        return args -> {
            if (userRepository.findByUsername("admin@example.com").isEmpty()) {
                userRepository.save(new User("admin", "adminych", "admin@example.com",
                        "$2a$10$zox3PUE57yMowF7WnSBjK.lgfqWEskUNIvRjSs35h60i1BRyG/X0O", Role.ADMIN));
            }

            if (userRepository.findByUsername("user@example.com").isEmpty()) {
                userRepository.save(new User("user", "userovich", "user@example.com",
                        "$2a$10$/DZ2UQRk.sJeEtTkETyq8.WkGqhuJVykS9NUiKN2iB3/Jllpk1.ki", Role.USER));
            }
        };
    }
}

