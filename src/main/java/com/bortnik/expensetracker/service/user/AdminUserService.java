package com.bortnik.expensetracker.service.user;

import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.entities.UserRole;
import com.bortnik.expensetracker.exceptions.user.UserNotFound;
import com.bortnik.expensetracker.repository.UserRepository;
import com.bortnik.expensetracker.util.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {
    private final UserRepository userRepository;

    @Transactional
    public void deleteUserById(final UUID userId, final String adminUsername) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            log.info("Admin with username = {} deleted user with id = {}", adminUsername, userId);
        } else {
            throw new UserNotFound("User with id = " + userId + " does not exist");
        }
    }

    @Transactional
    public UserDTO makeUserAdmin(final UUID userId, final String adminUsername) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setRole(UserRole.ADMIN);
                    user.setUpdatedAt(OffsetDateTime.now());
                    log.info("Admin with username = {} granted admin role to user with id = {}", adminUsername, userId);
                    return UserMapper.toDto(user);
                })
                .orElseThrow(() -> new UserNotFound("User with id = " + userId + " does not exist"));
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toDto);
    }
}
