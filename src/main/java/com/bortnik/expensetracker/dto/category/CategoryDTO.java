package com.bortnik.expensetracker.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CategoryDTO {
    private UUID id;
    private UUID userId;
    private String name;
}
