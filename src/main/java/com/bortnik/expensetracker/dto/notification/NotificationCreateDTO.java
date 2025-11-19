package com.bortnik.expensetracker.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class NotificationCreateDTO {
    @NotBlank(message = "user id required")
    UUID userId;
    @NotBlank(message = "message required")
    String message;
}
