package com.bortnik.expensetracker.dto.expenses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ExpensesDTO {
    private UUID id;
    private UUID userId;
    private UUID categoryId;
    private Double amount;
    private LocalDate date;
    private String description;
}
