package com.bortnik.expensetracker.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class CategoryCreateRequestDTO {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 255, message = "category name must be between 3 and 255 characters")
    private String name;
}
