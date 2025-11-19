package com.bortnik.expensetracker.dto.budget;

public record BudgetRowData(
        double limit,
        double spent,
        double remaining,
        double percentageDecimal,
        boolean isOverBudget
) {}