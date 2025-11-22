package com.bortnik.expensetracker.controller.user;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.user.UserDTO;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.user.UserService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/who-am-i")
    public ApiResponse<UserDTO> getWhoAmI(@AuthenticationPrincipal UserDetailsImpl user) {
        UserDTO userDTO = userService.getUserById(user.getId());
        return ApiResponseFactory.success(userDTO);
    }
}