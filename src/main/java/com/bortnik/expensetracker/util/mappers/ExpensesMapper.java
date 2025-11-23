package com.bortnik.expensetracker.util.mappers;

import com.bortnik.expensetracker.dto.expenses.ExpensesCreateDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import com.bortnik.expensetracker.entities.Expenses;

public class ExpensesMapper {
    public static Expenses toEntity(ExpensesCreateDTO expensesCreateDTO) {
        return Expenses.builder()
                .userId(expensesCreateDTO.getUserId())
                .categoryId(expensesCreateDTO.getCategoryId())
                .amount(expensesCreateDTO.getAmount())
                .date(expensesCreateDTO.getDate())
                .description(expensesCreateDTO.getDescription())
                .build();
    }

    public static ExpensesDTO toDto(Expenses expenses) {
        return ExpensesDTO.builder()
                .id(expenses.getId())
                .userId(expenses.getUserId())
                .categoryId(expenses.getCategoryId())
                .amount(expenses.getAmount())
                .date(expenses.getDate())
                .description(expenses.getDescription())
                .createdAt(expenses.getCreatedAt())
                .updatedAt(expenses.getUpdatedAt())
                .build();
    }
}
