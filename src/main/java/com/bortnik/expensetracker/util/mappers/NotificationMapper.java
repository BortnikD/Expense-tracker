package com.bortnik.expensetracker.util.mappers;

import com.bortnik.expensetracker.dto.notification.NotificationCreateDTO;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.entities.Notification;

public class NotificationMapper {
    public static Notification toEntity(NotificationCreateDTO notificationCreateDTO) {
        return Notification.builder()
                .userId(notificationCreateDTO.getUserId())
                .message(notificationCreateDTO.getMessage())
                .build();
    }

    public static NotificationDTO toDto(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .read(notification.isRead())
                .build();
    }
}
