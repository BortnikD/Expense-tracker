package com.bortnik.expensetracker.dto.expenses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ExpensesUpdateDTO {
    private UUID id;
    private UUID userId;
    private Double amount;
    private LocalDate date;
    private String description;
}
