package com.bortnik.expensetracker.exceptions.expenses;

public class ExpensesNotFound extends RuntimeException {
    public ExpensesNotFound(String message) {
        super(message);
    }
}
