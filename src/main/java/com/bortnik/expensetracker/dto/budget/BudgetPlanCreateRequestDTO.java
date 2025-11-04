package com.bortnik.expensetracker.dto.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class BudgetPlanCreateRequestDTO {
    private UUID categoryId;
    @NotBlank(message = "userId is required")
    @Positive(message = "Limit amount must be positive")
    private Double limitAmount;
    @NotBlank(message = "userId is required")
    private LocalDate month;
}
