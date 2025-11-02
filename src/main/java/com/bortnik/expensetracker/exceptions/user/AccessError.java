package com.bortnik.expensetracker.exceptions.user;

public class AccessError extends RuntimeException {
  public AccessError(String message) {
    super(message);
  }
}
