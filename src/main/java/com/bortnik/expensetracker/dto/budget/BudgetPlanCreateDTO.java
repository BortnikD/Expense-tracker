package com.bortnik.expensetracker.dto.budget;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class BudgetPlanCreateDTO {
    UUID userId;
    UUID categoryId;
    Double limitAmount;
    Double spentAmount;
    LocalDate month;
}
