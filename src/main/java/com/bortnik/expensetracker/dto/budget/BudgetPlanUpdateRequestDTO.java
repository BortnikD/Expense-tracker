package com.bortnik.expensetracker.dto.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BudgetPlanUpdateRequestDTO {
    @NotBlank
    private UUID id;
    @NotBlank
    @Positive(message = "Limit amount must be positive")
    private Double limitAmount;
}
