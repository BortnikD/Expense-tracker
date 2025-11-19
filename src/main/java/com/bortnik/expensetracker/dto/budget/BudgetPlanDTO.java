package com.bortnik.expensetracker.dto.budget;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class BudgetPlanDTO {
    UUID id;
    UUID userId;
    UUID categoryId;
    double limitAmount;
    double spentAmount;
    LocalDate month;
}
