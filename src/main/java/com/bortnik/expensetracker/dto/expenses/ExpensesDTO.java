package com.bortnik.expensetracker.dto.expenses;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class ExpensesDTO {
    UUID id;
    UUID userId;
    UUID categoryId;
    double amount;
    LocalDate date;
    String description;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
