package com.bortnik.expensetracker.util.mappers;

import com.bortnik.expensetracker.dto.user.UserCreateDTO;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.entities.User;

public class UserMapper {
    public static User toEntity(UserCreateDTO user) {
        return User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public static UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
