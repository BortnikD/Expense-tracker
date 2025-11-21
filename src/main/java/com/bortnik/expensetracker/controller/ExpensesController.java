package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.controller.validator.DateValidator;
import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.expenses.*;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.ExpensesService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Valid
public class ExpensesController {
    private final ExpensesService expensesService;

    @PostMapping
    public ApiResponse<ExpensesDTO> createExpenses(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ExpensesCreateRequestDTO expensesCreateRequestDTO
    ) {
        DateValidator.validateDateIsFuture(expensesCreateRequestDTO.getDate());
        ExpensesDTO expenses = expensesService.createExpenses(
                ExpensesCreateDTO.builder()
                        .userId(userDetails.getId())
                        .categoryId(expensesCreateRequestDTO.getCategoryId())
                        .amount(expensesCreateRequestDTO.getAmount())
                        .description(expensesCreateRequestDTO.getDescription())
                        .date(expensesCreateRequestDTO.getDate())
                        .build()
        );
        return ApiResponseFactory.success(expenses);
    }

    @GetMapping("/all-between-dates")
    public ApiResponse<List<ExpensesDTO>> getExpensesBetweenDates(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        DateValidator.validateEndDatePastStartDate(startDate, endDate);
        List<ExpensesDTO> expenses = expensesService.getExpensesBetweenDates(
                userDetails.getId(),
                startDate,
                endDate
        );
        return ApiResponseFactory.success(expenses);
    }

    @GetMapping("/all-by-category")
    public ApiResponse<List<ExpensesDTO>> getExpensesBetweenDatesByCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam UUID categoryId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        DateValidator.validateEndDatePastStartDate(startDate, endDate);
        List<ExpensesDTO> expenses = expensesService.getUserExpensesByCategoryBetweenDates(
                userDetails.getId(),
                categoryId,
                startDate,
                endDate
        );
        return ApiResponseFactory.success(expenses);
    }

    @GetMapping("/all-by-date")
    public ApiResponse<List<ExpensesDTO>> getExpensesByDate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam LocalDate date
    ) {
        List<ExpensesDTO> expenses = expensesService.getUserExpensesByDate(
                userDetails.getId(),
                date
        );
        return ApiResponseFactory.success(expenses);
    }

    @PatchMapping
    public ApiResponse<ExpensesDTO> updateExpenses(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ExpensesUpdateRequestDTO expensesUpdateRequestDTO
    ) {
        ExpensesDTO expenses = expensesService.updateExpenses(
                ExpensesUpdateDTO.builder()
                        .id(expensesUpdateRequestDTO.getId())
                        .userId(userDetails.getId())
                        .amount(expensesUpdateRequestDTO.getAmount())
                        .description(expensesUpdateRequestDTO.getDescription())
                        .build()
        );
        return ApiResponseFactory.success(expenses);
    }
}