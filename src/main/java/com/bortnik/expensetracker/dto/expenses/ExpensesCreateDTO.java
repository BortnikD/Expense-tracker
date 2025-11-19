package com.bortnik.expensetracker.dto.expenses;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class ExpensesCreateDTO {
    UUID userId;
    UUID categoryId;
    double amount;
    LocalDate date;
    String description;
}
