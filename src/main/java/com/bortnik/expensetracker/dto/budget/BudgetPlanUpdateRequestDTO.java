package com.bortnik.expensetracker.dto.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetPlanUpdateRequestDTO {
    @NotBlank
    private UUID id;
    @NotBlank
    @Positive(message = "Limit amount must be positive")
    private double limitAmount;
}
