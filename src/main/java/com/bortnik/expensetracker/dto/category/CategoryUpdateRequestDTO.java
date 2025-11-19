package com.bortnik.expensetracker.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class CategoryUpdateRequestDTO {

    @NotBlank(message = "id is required")
    private UUID id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 255, message = "category name must be between 3 and 255 characters")
    private String name;
}
