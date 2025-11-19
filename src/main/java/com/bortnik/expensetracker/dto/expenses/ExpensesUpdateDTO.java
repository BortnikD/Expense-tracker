package com.bortnik.expensetracker.dto.expenses;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ExpensesUpdateDTO {
    UUID id;
    UUID userId;
    double amount;
    String description;
}
