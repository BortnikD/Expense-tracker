package com.bortnik.expensetracker.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class NotificationDTO {
    UUID id;
    UUID userId;
    String message;
    OffsetDateTime createdAt;
    boolean read;
}
