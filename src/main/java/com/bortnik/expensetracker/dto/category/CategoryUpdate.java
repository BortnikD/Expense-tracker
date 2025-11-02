package com.bortnik.expensetracker.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class CategoryUpdate {
    private final UUID id;
    private final UUID userId;
    private final String name;
}
