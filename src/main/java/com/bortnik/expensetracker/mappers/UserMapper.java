package com.bortnik.expensetracker.mappers;

import com.bortnik.expensetracker.dto.user.CreateUserDTO;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.entities.User;

public class UserMapper {
    public static User toEntity(CreateUserDTO user) {
        return User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
