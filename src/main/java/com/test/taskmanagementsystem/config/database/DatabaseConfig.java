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
            userRepository.save(new User("Tolga", "Caglayan", "admin", "$2a$12$qr2Wa5upWw2/EjJP05s85.jDXE2N9nJXjBbKKwucoHHzf5Y041sVm", Role.ADMIN));
        };
    }



        public static void main(String[] args) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "password";  // Это ваш исходный пароль
            String encodedPassword = encoder.encode(rawPassword);  // Закодированный пароль
            System.out.println(encodedPassword);
        }
}

