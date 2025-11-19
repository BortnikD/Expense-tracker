package com.bortnik.expensetracker.dto.budget;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class BudgetUpdateExpenses {
    UUID id;
    double spentAmount;
}
