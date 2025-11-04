package com.bortnik.expensetracker.dto.expenses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ExpensesUpdateDTO {
    private UUID id;
    private UUID userId;
    private Double amount;
    private String description;
}
