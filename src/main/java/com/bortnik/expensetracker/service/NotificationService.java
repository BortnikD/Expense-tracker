package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.notification.NotificationCreateDTO;
import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import com.bortnik.expensetracker.entities.ExceededBudgetNotificationLog;
import com.bortnik.expensetracker.entities.Notification;
import com.bortnik.expensetracker.exceptions.notification.NotificationNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.util.mappers.NotificationMapper;
import com.bortnik.expensetracker.repository.ExceededBudgetNotificationLogRepository;
import com.bortnik.expensetracker.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ExceededBudgetNotificationLogRepository logRepository;
    private final WebSocketNotificationService webSocketNotificationService;
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private final ExceededBudgetNotificationLogRepository exceededBudgetNotificationLogRepository;

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

    @Transactional
    public void notifyUserAboutExceededLimit(BudgetPlanDTO exceededPlan) {

        final String budgetMonthKey = exceededPlan.getMonth().format(YEAR_MONTH_FORMATTER);

        final boolean alreadyNotified = logRepository.existsByUserIdAndBudgetMonthAndCategoryId(
                exceededPlan.getUserId(),
                budgetMonthKey,
                exceededPlan.getCategoryId()
        );

        if (alreadyNotified) {
            log.trace("Notification for user {} for plan '{}/{}' has already been sent. Skipping.",
                    exceededPlan.getUserId(), budgetMonthKey, exceededPlan.getCategoryId());
            return;
        }

        final String message = String.format(
                "Your budget plan for %s%s has exceeded the limit by %.2f units.",
                exceededPlan.getMonth().format(YEAR_MONTH_FORMATTER),
                exceededPlan.getCategoryId() != null
                        ? " (category ID: " + exceededPlan.getCategoryId() + ")"
                        : "",
                exceededPlan.getSpentAmount() - exceededPlan.getLimitAmount()
        );

        NotificationCreateDTO notificationDto = NotificationCreateDTO.builder()
                .userId(exceededPlan.getUserId())
                .message(message)
                .build();

        final NotificationDTO notification = createNotification(notificationDto);

        final ExceededBudgetNotificationLog logEntry = ExceededBudgetNotificationLog.builder()
                .userId(exceededPlan.getUserId())
                .budgetMonth(budgetMonthKey)
                .categoryId(exceededPlan.getCategoryId())
                .notifiedAt(OffsetDateTime.now())
                .build();

        logRepository.save(logEntry);

        log.info("Notification created for user {} for the budget of {}", exceededPlan.getUserId(), budgetMonthKey);

        webSocketNotificationService.sendNotificationToUser(notification);
    }

    @Transactional
    public void deleteNotActualNotifications() {
        OffsetDateTime startMonth = OffsetDateTime.now().withDayOfMonth(1);
        exceededBudgetNotificationLogRepository.deleteAllByNotifiedAtBefore(startMonth);
        log.info("Notifications before date {} were deleted", startMonth);
    }
}
