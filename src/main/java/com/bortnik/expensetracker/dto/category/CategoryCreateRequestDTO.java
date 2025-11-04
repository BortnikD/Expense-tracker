package com.bortnik.expensetracker.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryCreateRequestDTO {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 255, message = "category name must be between 3 and 255 characters")
    private final String name;
}
