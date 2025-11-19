package com.bortnik.expensetracker.dto.budget;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class BudgetPlanUpdateDTO {
    UUID id;
    UUID userId;
    double limitAmount;
}
