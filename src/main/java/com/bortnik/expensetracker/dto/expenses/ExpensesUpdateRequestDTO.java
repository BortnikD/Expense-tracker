package com.bortnik.expensetracker.dto.expenses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ExpensesUpdateRequestDTO {
    @NotNull(message = "id is required")
    private UUID id;
    @NotNull(message = "amount is required")
    @Positive(message = "Limit amount must be positive")
    private Double amount;
    @NotBlank(message = "description is required")
    @Size(min = 3, max = 512, message = "description must be between 3 and 512 characters")
    private String description;
}
