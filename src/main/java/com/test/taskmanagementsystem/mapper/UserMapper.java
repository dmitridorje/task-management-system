package com.test.taskmanagementsystem.mapper;

import com.test.taskmanagementsystem.model.dto.UserDto;
import com.test.taskmanagementsystem.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toUserDto(User user);
}
