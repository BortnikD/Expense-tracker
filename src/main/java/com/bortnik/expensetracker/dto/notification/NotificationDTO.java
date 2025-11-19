package com.bortnik.expensetracker.dto.notification;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class NotificationDTO {
    UUID id;
    UUID userId;
    String message;
    OffsetDateTime createdAt;
    boolean read;
}
