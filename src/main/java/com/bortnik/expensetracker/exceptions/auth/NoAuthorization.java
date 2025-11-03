package com.bortnik.expensetracker.exceptions.auth;

public class NoAuthorization extends RuntimeException {
    public NoAuthorization(String message) {
        super(message);
    }
}
