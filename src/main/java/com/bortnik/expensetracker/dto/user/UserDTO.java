package com.bortnik.expensetracker.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private OffsetDateTime createdAt;
}
