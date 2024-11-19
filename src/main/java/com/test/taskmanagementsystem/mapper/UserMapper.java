package com.test.taskmanagementsystem.mapper;

import com.test.taskmanagementsystem.model.UserDto;
import com.test.taskmanagementsystem.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUserEntity(UserDto userDto);
    List<UserDto> toUserDtos(List<User> users);
}
