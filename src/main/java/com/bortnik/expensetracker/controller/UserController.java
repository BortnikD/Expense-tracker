package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDTO getUserById(@RequestParam UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/by-username")
    public UserDTO getUserByUsername(
            @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
            @RequestParam
            String username
    ) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/by-email")
    public UserDTO getUserByEmail(
            @Email(message = "Email is invalid")
            @RequestParam
            String email
    ) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/who-am-i")
    public UserDTO getWhoAmI(@AuthenticationPrincipal UserDetails user ) {
        return userService.getUserByUsername(user.getUsername());
    }
}
