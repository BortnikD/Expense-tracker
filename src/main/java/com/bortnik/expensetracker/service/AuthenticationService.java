package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.AuthResponse;
import com.bortnik.expensetracker.dto.user.UserCreateDTO;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.dto.user.UserLogin;
import com.bortnik.expensetracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(UserCreateDTO createUserDTO) {
        createUserDTO.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        UserDTO user = userService.saveUser(createUserDTO);
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return AuthResponse.builder()
                .username(user.getUsername())
                .tokenType("Bearer")
                .token(token)
                .build();
    }

    public AuthResponse login(UserLogin userLogin) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword())
        );
        String token = jwtTokenProvider.generateToken(userLogin.getUsername());
        return AuthResponse.builder()
                .username(userLogin.getUsername())
                .tokenType("Bearer")
                .token(token)
                .build();
    }
}
