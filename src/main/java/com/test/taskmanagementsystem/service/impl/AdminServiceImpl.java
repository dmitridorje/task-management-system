package com.test.taskmanagementsystem.service.impl;

import com.test.taskmanagementsystem.mapper.UserMapper;
import com.test.taskmanagementsystem.model.dto.CreateUserDto;
import com.test.taskmanagementsystem.model.dto.UserDto;
import com.test.taskmanagementsystem.model.entity.User;
import com.test.taskmanagementsystem.repository.UserRepository;
import com.test.taskmanagementsystem.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto createUser(CreateUserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRole(user.getRole());
        return userMapper.toUserDto(userRepository.save(newUser));
    }
}
