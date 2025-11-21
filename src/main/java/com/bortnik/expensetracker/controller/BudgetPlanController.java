package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.controller.validator.DateValidator;
import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.budget.*;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.BudgetPlanService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
@Validated
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;

    @GetMapping
    public ApiResponse<BudgetPlanDTO> getBudgetPlanByUserIdAndCategoryIdAndMonth(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam LocalDate month
    ) {
        UUID userId = userDetails.getId();
        BudgetPlanDTO budgetPlanDTO = categoryId != null
                ? budgetPlanService.getBudgetPlanByUserIdAndCategoryIdAndMonth(userId, categoryId, month)
                : budgetPlanService.getBudgetPlanByUserIdAndMonth(userId, month);
        return ApiResponseFactory.success(budgetPlanDTO);
    }

    @GetMapping("/exceeding")
    public ApiResponse<List<BudgetPlanDTO>> getExceedingBudgetPlans(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getId();
        List<BudgetPlanDTO> budgetPlans = budgetPlanService.getExceedingBudgetPlans(userId);
        return ApiResponseFactory.success(budgetPlans);
    }

    @GetMapping("/all-in-month")
    public ApiResponse<List<BudgetPlanDTO>> getBudgetPlansByUserIdAndMonth(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam LocalDate month
    ) {
        UUID userId = userDetails.getId();
        List<BudgetPlanDTO> budgetPlans = budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month);
        return ApiResponseFactory.success(budgetPlans);
    }

    @GetMapping("/all")
    public ApiResponse<Page<BudgetPlanDTO>> getAllBudgetPlans(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PositiveOrZero(message = "page must be positive or zero")
            @RequestParam int page,
            @PositiveOrZero(message = "page size must be positive or zero")
            @RequestParam int pageSize
    ) {
        UUID userId = userDetails.getId();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        Page<BudgetPlanDTO> budgetPlans = budgetPlanService.getAllBudgetPlans(userId, pageable);
        return ApiResponseFactory.success(budgetPlans);
    }

    @PostMapping
    public ApiResponse<BudgetPlanDTO> createBudgetPlan(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BudgetPlanCreateRequestDTO budgetPlanCreateRequestDTO
    ) {
        DateValidator.validateDateIsFuture(budgetPlanCreateRequestDTO.getMonth());
        BudgetPlanDTO budgetPlan = budgetPlanService.createBudgetPlan(
                BudgetPlanCreateDTO.builder()
                        .userId(userDetails.getId())
                        .categoryId(budgetPlanCreateRequestDTO.getCategoryId())
                        .limitAmount(budgetPlanCreateRequestDTO.getLimitAmount())
                        .spentAmount(0.0)
                        .month(budgetPlanCreateRequestDTO.getMonth())
                        .build()
        );
        return ApiResponseFactory.success(budgetPlan);
    }

    @PatchMapping
    public ApiResponse<BudgetPlanDTO> updateBudgetPlanLimit(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BudgetPlanUpdateRequestDTO budgetPlanUpdateRequestDTO
    ) {
        BudgetPlanDTO budgetPlan = budgetPlanService.updateBudgetPlan(
                BudgetPlanUpdateDTO.builder()
                        .id(budgetPlanUpdateRequestDTO.getId())
                        .userId(userDetails.getId())
                        .limitAmount(budgetPlanUpdateRequestDTO.getLimitAmount())
                        .build()
        );
        return ApiResponseFactory.success(budgetPlan);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<BudgetPlanDTO> deleteBudgetPlan(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id
    ) {
        UUID userId = userDetails.getId();
        BudgetPlanDTO budgetPlan = budgetPlanService.deleteBudgetPlan(id, userId);
        return ApiResponseFactory.success(budgetPlan);
    }
}