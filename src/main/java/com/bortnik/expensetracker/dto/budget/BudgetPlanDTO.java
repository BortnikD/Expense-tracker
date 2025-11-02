package com.bortnik.expensetracker.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class BudgetPlanDTO {
    private UUID id;
    private UUID userId;
    private UUID categoryId;
    private Double limitAmount;
    private Double spentAmount;
    private LocalDate month;
}
