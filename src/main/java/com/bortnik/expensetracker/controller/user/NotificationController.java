package com.bortnik.expensetracker.controller.user;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.NotificationService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/count")
    public ApiResponse<Long> getCountOfUnreadUserMessages(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        final UUID userId = userDetails.getId();
        final Long count = notificationService.getCountOfUnreadUserMessages(userId);
        return ApiResponseFactory.success(count);
    }

    @GetMapping("/all")
    public ApiResponse<Page<NotificationDTO>> getNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        final UUID userId = userDetails.getId();
        final Page<NotificationDTO> notifications = notificationService.getNotifications(userId, pageable);
        return ApiResponseFactory.success(notifications);
    }

    @GetMapping("/by-read-status")
    public ApiResponse<Page<NotificationDTO>> getNotificationsByRead(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam boolean isRead,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        final UUID userId = userDetails.getId();
        final Page<NotificationDTO> notifications = notificationService.getNotificationsByRead(userId, isRead, pageable);
        return ApiResponseFactory.success(notifications);
    }

    @PatchMapping("/{notificationId}/mark-as-read")
    public ApiResponse<Void> markNotificationAsRead(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID notificationId
    ) {
        final UUID userId = userDetails.getId();
        notificationService.markNotificationAsRead(notificationId, userId);
        return ApiResponseFactory.success(null);
    }
}
