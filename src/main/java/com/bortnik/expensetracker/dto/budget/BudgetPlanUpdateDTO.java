package com.bortnik.expensetracker.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class BudgetPlanUpdateDTO {
    private UUID id;
    private UUID userId;
    private Double limitAmount;
    private Double spentAmount;
}
