package com.bortnik.expensetracker.exceptions.auth;

public class InvalidJwtToken extends RuntimeException {
    public InvalidJwtToken(String message) {
        super(message);
    }
}
