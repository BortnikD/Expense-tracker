package com.bortnik.expensetracker.exceptions.notification;

public class NotificationNotFound extends RuntimeException {
    public NotificationNotFound(String message) {
        super(message);
    }
}
