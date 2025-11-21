package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.UserService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ApiResponseFactory.success(user);
    }

    @GetMapping("/by-username")
    public ApiResponse<UserDTO> getUserByUsername(
            @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
            @RequestParam
            String username
    ) {
        UserDTO user = userService.getUserByUsername(username);
        return ApiResponseFactory.success(user);
    }

    @GetMapping("/by-email")
    public ApiResponse<UserDTO> getUserByEmail(
            @Email(message = "Email is invalid")
            @RequestParam
            String email
    ) {
        UserDTO user = userService.getUserByEmail(email);
        return ApiResponseFactory.success(user);
    }

    @GetMapping("/who-am-i")
    public ApiResponse<UserDTO> getWhoAmI(@AuthenticationPrincipal UserDetailsImpl user) {
        UserDTO userDTO = userService.getUserById(user.getId());
        return ApiResponseFactory.success(userDTO);
    }
}