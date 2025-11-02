package com.bortnik.expensetracker.mappers;

import com.bortnik.expensetracker.dto.expenses.ExpensesCreateDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import com.bortnik.expensetracker.entities.Expenses;

import java.util.List;

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
                .build();
    }

    public static List<ExpensesDTO> toDtoList(final List<Expenses> expensesList) {
        return expensesList.stream()
                .map(ExpensesMapper::toDto)
                .toList();
    }
}
