package com.test.taskmanagementsystem.service;

import com.test.taskmanagementsystem.model.dto.requestdtos.CreateUserDto;
import com.test.taskmanagementsystem.model.dto.UserDto;

public interface AdminService {

    UserDto createUser(CreateUserDto user);
}
