package com.bortnik.expensetracker.exceptions.budget;

public class BudgetPlanNotFound extends RuntimeException {
    public BudgetPlanNotFound(String message) {
        super(message);
    }
}
