package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.user.CreateUserDTO;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.exceptions.user.UserNotFound;
import com.bortnik.expensetracker.exceptions.user.UserAlreadyExists;
import com.bortnik.expensetracker.mappers.UserMapper;
import com.bortnik.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO saveUser(CreateUserDTO user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExists("user with username = " + user.getUsername() + " already exists");
        }
        else if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExists("user with email = " + user.getEmail() + " already exists");
        }
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(user)));
    }

    public UserDTO getUserById(UUID id) {
        return UserMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFound("User with id = " + id + " does not exist"))
        );
    }

    public UserDTO getUserByUsername(String username) {
        return UserMapper.toDto(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFound("User with username = " + username + " does not exist"))
        );
    }

    public UserDTO getUserByEmail(String email) {
        return UserMapper.toDto(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFound("User with email = " + email + " does not exist"))
        );
    }

    public boolean existsUserById(UUID id) {
        return userRepository.existsById(id);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
