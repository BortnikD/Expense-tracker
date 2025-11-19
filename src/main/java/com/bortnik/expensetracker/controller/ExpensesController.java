package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.expenses.*;
import com.bortnik.expensetracker.exceptions.BadRequest;
import com.bortnik.expensetracker.service.ExpensesService;
import com.bortnik.expensetracker.service.UserService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

    @PostMapping
    public ApiResponse<ExpensesDTO> createExpenses(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ExpensesCreateRequestDTO expensesCreateRequestDTO
    ) {
        validateDate(expensesCreateRequestDTO.getDate());
        ExpensesDTO expenses = expensesService.createExpenses(
                ExpensesCreateDTO.builder()
                        .userId(userService.getUserByUsername(userDetails.getUsername()).getId())
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
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<ExpensesDTO> expenses = expensesService.getExpensesBetweenDates(
                userService.getUserByUsername(userDetails.getUsername()).getId(),
                startDate,
                endDate
        );
        return ApiResponseFactory.success(expenses);
    }

    @GetMapping("/all-by-category")
    public ApiResponse<List<ExpensesDTO>> getExpensesBetweenDatesByCategory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID categoryId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<ExpensesDTO> expenses = expensesService.getUserExpensesByCategoryBetweenDates(
                userService.getUserByUsername(userDetails.getUsername()).getId(),
                categoryId,
                startDate,
                endDate
        );
        return ApiResponseFactory.success(expenses);
    }

    @GetMapping("/all-by-date")
    public ApiResponse<List<ExpensesDTO>> getExpensesByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate date
    ) {
        List<ExpensesDTO> expenses = expensesService.getUserExpensesByDate(
                userService.getUserByUsername(userDetails.getUsername()).getId(),
                date
        );
        return ApiResponseFactory.success(expenses);
    }

    @PatchMapping
    public ApiResponse<ExpensesDTO> updateExpenses(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ExpensesUpdateRequestDTO expensesUpdateRequestDTO
    ) {
        ExpensesDTO expenses = expensesService.updateExpenses(
                ExpensesUpdateDTO.builder()
                        .id(expensesUpdateRequestDTO.getId())
                        .userId(userService.getUserByUsername(userDetails.getUsername()).getId())
                        .amount(expensesUpdateRequestDTO.getAmount())
                        .description(expensesUpdateRequestDTO.getDescription())
                        .build()
        );
        return ApiResponseFactory.success(expenses);
    }

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BadRequest("Date cannot be before today");
        }
    }
}