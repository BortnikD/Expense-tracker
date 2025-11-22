package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.auth.AuthResponse;
import com.bortnik.expensetracker.dto.auth.UserRegister;
import com.bortnik.expensetracker.dto.auth.UserLogin;
import com.bortnik.expensetracker.service.AuthenticationService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
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
    public ApiResponse<AuthResponse> registerUser(@Valid @RequestBody UserRegister userCreateDTO) {
        final AuthResponse authResponse = authenticationService.register(userCreateDTO);
        return ApiResponseFactory.success(authResponse);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> loginUser(@Valid @RequestBody UserLogin userLogin) {
        final AuthResponse authResponse = authenticationService.login(userLogin);
        return ApiResponseFactory.success(authResponse);
    }
}
