package com.bortnik.expensetracker.dto.category;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class CategoryDTO {
    UUID id;
    UUID userId;
    String name;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
