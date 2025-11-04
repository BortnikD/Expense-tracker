package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.notification.NotificationCreateDTO;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.service.NotificationService;
import com.bortnik.expensetracker.service.UserService;
import jakarta.validation.Valid;
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

    /** WARNING
     * Only for tests. Delete it in production
     */
//    @PostMapping
//    public NotificationDTO createNotification(
//            @Valid
//            @RequestBody
//            NotificationCreateDTO notificationCreateDTO
//    ) {
//        return notificationService.createNotification(notificationCreateDTO);
//    }

    @GetMapping("/all")
    public Page<NotificationDTO> getNotifications(
            @AuthenticationPrincipal UserDetails user,
            @PositiveOrZero(message = "page must be positive or zero" )
            @RequestParam int page,
            @Positive(message = "page size must be positive" )
            @RequestParam int pageSize
    ) {
        UUID userId = userService.getUserByUsername(user.getUsername()).getId();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        return notificationService.getNotifications(userId, pageable);
    }

    @GetMapping("/by-read-status")
    public Page<NotificationDTO> getNotificationsByRead(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam boolean isRead,
            @PositiveOrZero(message = "page must be positive or zero" )
            @RequestParam int page,
            @Positive(message = "page size must be positive" )
            @RequestParam int pageSize
    ) {
        UUID userId = userService.getUserByUsername(user.getUsername()).getId();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        return notificationService.getNotificationsByRead(userId, isRead, pageable);
    }

    @PatchMapping("/{notificationId}/mark-as-read")
    public void markNotificationAsRead(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID notificationId
    ) {
        UUID userId = userService.getUserByUsername(user.getUsername()).getId();
        notificationService.markNotificationAsRead(notificationId, userId);
    }
}
