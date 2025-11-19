package com.bortnik.expensetracker.dto.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetPlanCreateRequestDTO {
    private UUID categoryId;
    @NotBlank(message = "userId is required")
    @Positive(message = "Limit amount must be positive")
    private double limitAmount;
    @NotBlank(message = "userId is required")
    private LocalDate month;
}
