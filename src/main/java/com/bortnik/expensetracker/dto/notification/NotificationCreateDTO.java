package com.bortnik.expensetracker.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class NotificationCreateDTO {
    @NotBlank(message = "user id required")
    UUID userId;
    @NotBlank(message = "message required")
    String message;
}
