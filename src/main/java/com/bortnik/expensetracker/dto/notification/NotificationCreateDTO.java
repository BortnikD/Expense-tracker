package com.bortnik.expensetracker.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class NotificationCreateDTO {
    UUID userId;
    String message;
}
