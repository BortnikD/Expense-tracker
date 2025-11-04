package com.bortnik.expensetracker.dto.budget;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BudgetUpdateExpenses {
    UUID id;
    double spentAmount;
}
