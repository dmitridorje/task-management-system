package com.test.taskmanagementsystem.service;

import com.test.taskmanagementsystem.model.dto.NewUserDto;
import com.test.taskmanagementsystem.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailService();
}