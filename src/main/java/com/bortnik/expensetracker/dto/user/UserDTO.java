package com.bortnik.expensetracker.dto.user;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class UserDTO {
    UUID id;
    String username;
    String email;
    OffsetDateTime createdAt;
}
