package com.bortnik.expensetracker.controller.admin;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.user.AdminUserService;
import com.bortnik.expensetracker.service.user.UserService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final AdminUserService adminUserService;

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable UUID id) {
        final UserDTO user = userService.getUserById(id);
        return ApiResponseFactory.success(user);
    }

    @GetMapping("/by-username")
    public ApiResponse<UserDTO> getUserByUsername(
            @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
            @RequestParam String username
    ) {
        final UserDTO user = userService.getUserByUsername(username);
        return ApiResponseFactory.success(user);
    }

    @GetMapping("/by-email")
    public ApiResponse<UserDTO> getUserByEmail(
            @Email(message = "Email is invalid")
            @RequestParam String email
    ) {
        final UserDTO user = userService.getUserByEmail(email);
        return ApiResponseFactory.success(user);
    }

    @GetMapping
    public ApiResponse<Page<UserDTO>> getAllUsers(
            @PageableDefault(
                    size = 20,
                    sort="createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        final Page<UserDTO> users = adminUserService.getAllUsers(pageable);
        return ApiResponseFactory.success(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @AuthenticationPrincipal UserDetailsImpl adminUser,
            @PathVariable UUID id
    ) {
        adminUserService.deleteUserById(id, adminUser.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/make-admin")
    public ApiResponse<UserDTO> makeUserAdmin(
            @AuthenticationPrincipal UserDetailsImpl adminUser,
            @PathVariable UUID id
    ) {
        final UserDTO user = adminUserService.makeUserAdmin(id, adminUser.getUsername());
        return ApiResponseFactory.success(user);
    }
}
