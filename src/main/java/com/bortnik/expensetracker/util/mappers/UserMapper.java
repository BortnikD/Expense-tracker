package com.bortnik.expensetracker.util.mappers;

import com.bortnik.expensetracker.dto.user.UserRegister;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.entities.User;

public class UserMapper {
    public static User toEntity(UserRegister user) {
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
