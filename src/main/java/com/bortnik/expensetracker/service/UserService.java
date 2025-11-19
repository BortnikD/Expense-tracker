package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.user.UserRegister;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.exceptions.user.UserNotFound;
import com.bortnik.expensetracker.exceptions.user.UserAlreadyExists;
import com.bortnik.expensetracker.mappers.UserMapper;
import com.bortnik.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDTO saveUser(UserRegister user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExists("user with username = " + user.getUsername() + " already exists");
        }
        else if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExists("user with email = " + user.getEmail() + " already exists");
        }
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(user)));
    }

    @Cacheable(value = "usersById", key = "#id")
    public UserDTO getUserById(UUID id) {
        return UserMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFound("User with id = " + id + " does not exist"))
        );
    }

    @Cacheable(value = "usersByUsername", key = "#username")
    public UserDTO getUserByUsername(String username) {
        return UserMapper.toDto(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFound("User with username = " + username + " does not exist"))
        );
    }

    @Cacheable(value = "usersByEmail", key = "#email")
    public UserDTO getUserByEmail(String email) {
        return UserMapper.toDto(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFound("User with email = " + email + " does not exist"))
        );
    }
}
