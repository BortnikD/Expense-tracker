package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.notification.NotificationCreateDTO;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.entities.Notification;
import com.bortnik.expensetracker.exceptions.notification.NotificationNotFound;
import com.bortnik.expensetracker.mappers.NotificationMapper;
import com.bortnik.expensetracker.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification saveNotification(final NotificationCreateDTO notificationCreateDTO) {
        return notificationRepository.save(NotificationMapper.toEntity(notificationCreateDTO));
    }

    public Page<NotificationDTO> getNotifications(
            final UUID userId,
            final Pageable pageable
    ) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(NotificationMapper::toDto);
    }

    public Page<NotificationDTO> getNotificationsByRead(
            final UUID userId,
            final boolean isRead,
            final Pageable pageable
    ) {
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, isRead, pageable)
                .map(NotificationMapper::toDto);
    }

    @Transactional
    public void markNotificationAsRead(final UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new NotificationNotFound("Notification with id " + notificationId + " does not exist"));

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
