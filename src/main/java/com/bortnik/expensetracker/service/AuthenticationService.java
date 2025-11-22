package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.AuthResponse;
import com.bortnik.expensetracker.dto.user.UserCreateDTO;
import com.bortnik.expensetracker.dto.user.UserRegister;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.dto.user.UserLogin;
import com.bortnik.expensetracker.entities.UserRole;
import com.bortnik.expensetracker.exceptions.BadCredentials;
import com.bortnik.expensetracker.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(UserRegister userRegister) {
        log.info("Registering new user: {}", userRegister.getUsername());

        final UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username(userRegister.getUsername())
                .email(userRegister.getEmail())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .role(UserRole.USER)
                .build();
        final UserDTO user = userService.saveUser(userCreateDTO);

        final String token = jwtTokenProvider.generateToken(
                user.getUsername(),
                List.of(user.getRole().toString())
        );

        log.info("User with username {} registered successfully", user.getUsername());

        return AuthResponse.builder()
                .username(user.getUsername())
                .tokenType("Bearer")
                .token(token)
                .build();
    }

    public AuthResponse login(UserLogin userLogin) {
        try {
            final Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword())
            );

            final Collection<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            final String token = jwtTokenProvider.generateToken(userLogin.getUsername(), roles);

            log.info("User with username {} logged in successfully", userLogin.getUsername());

            return AuthResponse.builder()
                    .username(userLogin.getUsername())
                    .tokenType("Bearer")
                    .token(token)
                    .build();
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for user: {}", userLogin.getUsername());
            throw new BadCredentials("Invalid username or password");
        }
    }
}
