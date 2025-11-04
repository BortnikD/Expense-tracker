package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.notification.NotificationCreateDTO;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.entities.Notification;
import com.bortnik.expensetracker.exceptions.notification.NotificationNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceTests {

    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final NotificationService notificationService = new NotificationService(notificationRepository);

    private final UUID notificationId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final UUID otherUserId = UUID.randomUUID();
    private final String message = "Test notification";
    private final OffsetDateTime createdAt = OffsetDateTime.now();

    private final Notification notification = Notification.builder()
            .id(notificationId)
            .userId(userId)
            .message(message)
            .createdAt(createdAt)
            .read(false)
            .build();

    private final NotificationDTO notificationDTO = NotificationDTO.builder()
            .id(notificationId)
            .userId(userId)
            .message(message)
            .createdAt(createdAt)
            .read(false)
            .build();

    private final NotificationCreateDTO createDTO = NotificationCreateDTO.builder()
            .userId(userId)
            .message(message)
            .build();

    private final Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    @Test
    void saveNotification_ShouldSaveSuccessfully() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        final NotificationDTO saved = notificationService.createNotification(createDTO);

        assertEquals(notificationDTO, saved);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void getNotifications_ShouldReturnMappedPage() {
        Page<Notification> notificationPage = new PageImpl<>(List.of(notification), pageable, 1);
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(notificationPage);

        final Page<NotificationDTO> result = notificationService.getNotifications(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(notificationDTO, result.getContent().getFirst());
        verify(notificationRepository, times(1)).findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Test
    void getNotifications_ShouldReturnEmptyPage_WhenNoNotifications() {
        Page<Notification> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(emptyPage);

        final Page<NotificationDTO> result = notificationService.getNotifications(userId, pageable);

        assertEquals(0, result.getTotalElements());
        assertEquals(List.of(), result.getContent());
    }

    @Test
    void getNotificationsByRead_ShouldReturnMappedPage() {
        Page<Notification> notificationPage = new PageImpl<>(List.of(notification), pageable, 1);
        when(notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false, pageable))
                .thenReturn(notificationPage);

        final Page<NotificationDTO> result = notificationService.getNotificationsByRead(userId, false, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(notificationDTO, result.getContent().getFirst());
        verify(notificationRepository, times(1))
                .findByUserIdAndReadOrderByCreatedAtDesc(userId, false, pageable);
    }

    @Test
    void getNotificationsByRead_ShouldReturnEmptyPage_WhenNoNotifications() {
        Page<Notification> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, true, pageable))
                .thenReturn(emptyPage);

        final Page<NotificationDTO> result = notificationService.getNotificationsByRead(userId, true, pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void markNotificationAsRead_ShouldSetReadTrue_AndSave_WhenNotificationExists() {
        Notification testNotification = Notification.builder()
                .id(notificationId)
                .userId(userId)
                .message(message)
                .read(false)
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(testNotification));

        notificationService.markNotificationAsRead(notificationId, userId);

        assertTrue(testNotification.isRead());
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(testNotification);
        verifyNoMoreInteractions(notificationRepository);
    }

    @Test
    void markNotificationAsRead_ShouldThrowException_WhenNotificationNotFound() {
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        NotificationNotFound exception = assertThrows(
                NotificationNotFound.class,
                () -> notificationService.markNotificationAsRead(notificationId, userId)
        );

        assertEquals("Notification with id " + notificationId + " does not exist", exception.getMessage());

        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository, never()).save(any());
        verifyNoMoreInteractions(notificationRepository);
    }

    @Test
    void markNotificationAsRead_ShouldThrowAccessError_WhenUserDoesNotOwnNotification() {
        Notification testNotification = Notification.builder()
                .id(notificationId)
                .userId(otherUserId)
                .message(message)
                .read(false)
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(testNotification));

        AccessError exception = assertThrows(
                AccessError.class,
                () -> notificationService.markNotificationAsRead(notificationId, userId)
        );

        assertEquals("You do not have access to this notification", exception.getMessage());

        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository, never()).save(any());
        verifyNoMoreInteractions(notificationRepository);
    }
}