package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.service.NotificationService;
import com.bortnik.expensetracker.service.UserService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/all")
    public ApiResponse<Page<NotificationDTO>> getNotifications(
            @AuthenticationPrincipal UserDetails user,
            @PositiveOrZero(message = "page must be positive or zero")
            @RequestParam int page,
            @Positive(message = "page size must be positive")
            @RequestParam int pageSize
    ) {
        UUID userId = userService.getUserByUsername(user.getUsername()).getId();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        Page<NotificationDTO> notifications = notificationService.getNotifications(userId, pageable);
        return ApiResponseFactory.success(notifications);
    }

    @GetMapping("/by-read-status")
    public ApiResponse<Page<NotificationDTO>> getNotificationsByRead(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam boolean isRead,
            @PositiveOrZero(message = "page must be positive or zero")
            @RequestParam int page,
            @Positive(message = "page size must be positive")
            @RequestParam int pageSize
    ) {
        UUID userId = userService.getUserByUsername(user.getUsername()).getId();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        Page<NotificationDTO> notifications = notificationService.getNotificationsByRead(userId, isRead, pageable);
        return ApiResponseFactory.success(notifications);
    }

    @PatchMapping("/{notificationId}/mark-as-read")
    public ApiResponse<Void> markNotificationAsRead(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID notificationId
    ) {
        UUID userId = userService.getUserByUsername(user.getUsername()).getId();
        notificationService.markNotificationAsRead(notificationId, userId);
        return ApiResponseFactory.success(null);
    }
}