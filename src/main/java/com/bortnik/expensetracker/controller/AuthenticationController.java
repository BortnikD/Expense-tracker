package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.AuthResponse;
import com.bortnik.expensetracker.dto.user.UserCreateDTO;
import com.bortnik.expensetracker.dto.user.UserLogin;
import com.bortnik.expensetracker.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthResponse registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return authenticationService.register(userCreateDTO);
    }

    @PostMapping("/login")
    public AuthResponse loginUser(@Valid @RequestBody UserLogin userLogin) {
        return authenticationService.login(userLogin);
    }
}
