package com.bortnik.expensetracker.dto.expenses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class ExpensesCreateRequestDTO {
    private UUID categoryId;

    @NotNull(message = "amount is required")
    @Positive(message = "Limit amount must be positive")
    private Double amount;

    @NotNull(message = "date is required")
    private LocalDate date;

    @NotBlank(message = "description is required")
    @Size(min = 3, max = 512, message = "description must be between 3 and 512 characters")
    private String description;
}
