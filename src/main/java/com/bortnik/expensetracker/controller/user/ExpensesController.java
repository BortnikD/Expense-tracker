package com.bortnik.expensetracker.controller.user;

import com.bortnik.expensetracker.controller.validator.DateValidator;
import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.expenses.*;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.ExpensesService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        final ExpensesDTO expenses = expensesService.createExpenses(
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
    public ApiResponse<Page<ExpensesDTO>> getExpensesBetweenDates(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        DateValidator.validateEndDatePastStartDate(startDate, endDate);
        final Page<ExpensesDTO> expenses = (categoryId  != null)
                ? expensesService.getUserExpensesByCategoryBetweenDates(
                        userDetails.getId(),
                        categoryId,
                        startDate,
                        endDate,
                        pageable
                )
                : expensesService.getExpensesBetweenDates(
                        userDetails.getId(),
                        startDate,
                        endDate,
                        pageable
                );
        return ApiResponseFactory.success(expenses);
    }

    @GetMapping("/all-by-date")
    public ApiResponse<Page<ExpensesDTO>> getExpensesByDate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam LocalDate date,
            @RequestParam(required = false) UUID categoryId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        final Page<ExpensesDTO> expenses = (categoryId != null)
                ? expensesService.getUserExpensesByDateAndCategory(userDetails.getId(), categoryId, date, pageable)
                : expensesService.getUserExpensesByDate(userDetails.getId(), date, pageable);
        return ApiResponseFactory.success(expenses);
    }

    @PatchMapping
    public ApiResponse<ExpensesDTO> updateExpenses(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ExpensesUpdateRequestDTO expensesUpdateRequestDTO
    ) {
        final ExpensesDTO expenses = expensesService.updateExpenses(
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
