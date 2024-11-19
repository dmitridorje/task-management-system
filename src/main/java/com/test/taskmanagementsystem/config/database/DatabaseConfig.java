package com.test.taskmanagementsystem.config.database;

import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.model.enums.Role;
import com.test.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {
    private final UserRepository userRepository;


    //@Bean
    public CommandLineRunner init() {
        return args -> {
            userRepository.save(new User("admin", "adminych", "admin@example.com",
                    "$2a$10$zox3PUE57yMowF7WnSBjK.lgfqWEskUNIvRjSs35h60i1BRyG/X0O", Role.ADMIN));
        };
    }



        public static void main(String[] args) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "password";  // Это ваш исходный пароль
            String encodedPassword = encoder.encode(rawPassword);  // Закодированный пароль
            System.out.println(encodedPassword);
        }
}

