package com.bortnik.expensetracker.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CreateCategoryDTO {

    @NotBlank(message = "userId is required")
    @Positive(message = "userId must be positive")
    private final UUID userId;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 255, message = "category name must be between 3 and 255 characters")
    private final String name;
}
