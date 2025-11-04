package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.notification.NotificationCreateDTO;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.entities.Notification;
import com.bortnik.expensetracker.exceptions.notification.NotificationNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.mappers.NotificationMapper;
import com.bortnik.expensetracker.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationDTO createNotification(final NotificationCreateDTO notificationCreateDTO) {
        return NotificationMapper.toDto(
                notificationRepository.save(
                        NotificationMapper.toEntity(notificationCreateDTO)
                )
        );
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
    public void markNotificationAsRead(final UUID notificationId, final UUID userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new NotificationNotFound("Notification with id " + notificationId + " does not exist"));

        if (!notification.getUserId().equals(userId)) {
            throw new AccessError("You do not have access to this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
