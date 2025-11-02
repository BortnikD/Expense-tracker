package com.bortnik.expensetracker.exceptions.budget;

public class BudgetPlanAlreadyExists extends RuntimeException {
    public BudgetPlanAlreadyExists(String message) {
        super(message);
    }
}
