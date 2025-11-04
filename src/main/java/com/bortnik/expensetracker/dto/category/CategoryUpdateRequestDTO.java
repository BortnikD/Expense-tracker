package com.bortnik.expensetracker.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CategoryUpdateRequestDTO {

    @NotBlank(message = "id is required")
    private final UUID id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 255, message = "category name must be between 3 and 255 characters")
    private final String name;
}
