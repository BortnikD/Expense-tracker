package com.bortnik.expensetracker.dto.category;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CategoryDTO {
    UUID id;
    UUID userId;
    String name;
}
