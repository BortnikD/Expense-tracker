package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.budget.*;
import com.bortnik.expensetracker.exceptions.BadRequest;
import com.bortnik.expensetracker.service.BudgetPlanService;
import com.bortnik.expensetracker.service.UserService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

    @GetMapping
    public ApiResponse<BudgetPlanDTO> getBudgetPlanByUserIdAndCategoryIdAndMonth(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam LocalDate month
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        BudgetPlanDTO budgetPlanDTO = categoryId != null
                ? budgetPlanService.getBudgetPlanByUserIdAndCategoryIdAndMonth(userId, categoryId, month)
                : budgetPlanService.getBudgetPlanByUserIdAndMonth(userId, month);
        return ApiResponseFactory.success(budgetPlanDTO);
    }

    @GetMapping("/exceeding")
    public ApiResponse<List<BudgetPlanDTO>> getExceedingBudgetPlans(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        List<BudgetPlanDTO> budgetPlans = budgetPlanService.getExceedingBudgetPlans(userId);
        return ApiResponseFactory.success(budgetPlans);
    }

    @GetMapping("/all-in-month")
    public ApiResponse<List<BudgetPlanDTO>> getBudgetPlansByUserIdAndMonth(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate month
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        List<BudgetPlanDTO> budgetPlans = budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month);
        return ApiResponseFactory.success(budgetPlans);
    }

    @GetMapping("/all")
    public ApiResponse<Page<BudgetPlanDTO>> getAllBudgetPlans(
            @AuthenticationPrincipal UserDetails userDetails,
            @PositiveOrZero(message = "page must be positive or zero")
            @RequestParam int page,
            @PositiveOrZero(message = "page size must be positive or zero")
            @RequestParam int pageSize
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        Page<BudgetPlanDTO> budgetPlans = budgetPlanService.getAllBudgetPlans(userId, pageable);
        return ApiResponseFactory.success(budgetPlans);
    }

    @PostMapping
    public ApiResponse<BudgetPlanDTO> createBudgetPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BudgetPlanCreateRequestDTO budgetPlanCreateRequestDTO
    ) {
        validateDate(budgetPlanCreateRequestDTO.getMonth());
        BudgetPlanDTO budgetPlan = budgetPlanService.createBudgetPlan(
                BudgetPlanCreateDTO.builder()
                        .userId(userService.getUserByUsername(userDetails.getUsername()).getId())
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
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BudgetPlanUpdateRequestDTO budgetPlanUpdateRequestDTO
    ) {
        BudgetPlanDTO budgetPlan = budgetPlanService.updateBudgetPlan(
                BudgetPlanUpdateDTO.builder()
                        .id(budgetPlanUpdateRequestDTO.getId())
                        .userId(userService.getUserByUsername(userDetails.getUsername()).getId())
                        .limitAmount(budgetPlanUpdateRequestDTO.getLimitAmount())
                        .build()
        );
        return ApiResponseFactory.success(budgetPlan);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<BudgetPlanDTO> deleteBudgetPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        BudgetPlanDTO budgetPlan = budgetPlanService.deleteBudgetPlan(id, userId);
        return ApiResponseFactory.success(budgetPlan);
    }

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BadRequest("Date cannot be before today");
        }
    }
}
