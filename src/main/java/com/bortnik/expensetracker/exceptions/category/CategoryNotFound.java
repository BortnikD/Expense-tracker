package com.bortnik.expensetracker.exceptions.category;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(String message) {
        super(message);
    }
}
