package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.budget.*;
import com.bortnik.expensetracker.exceptions.BadRequest;
import com.bortnik.expensetracker.service.BudgetPlanService;
import com.bortnik.expensetracker.service.UserService;
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
    public BudgetPlanDTO getBudgetPlanByUserIdAndCategoryIdAndMonth(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam LocalDate month
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        if (categoryId != null) {
            return budgetPlanService.getBudgetPlanByUserIdAndCategoryIdAndMonth(userId, categoryId, month);
        }
        else {
            return budgetPlanService.getBudgetPlanByUserIdAndMonth(userId, month);
        }
    }

    @GetMapping("/exceeding")
    public List<BudgetPlanDTO> getExceedingBudgetPlans(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        return budgetPlanService.getExceedingBudgetPlans(userId);
    }

    @GetMapping("/all-in-month")
    public List<BudgetPlanDTO> getBudgetPlansByUserIdAndMonth(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate month
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        return budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month);
    }

    @GetMapping("/all")
    public Page<BudgetPlanDTO> getAllBudgetPlans(
            @AuthenticationPrincipal UserDetails userDetails,
            @PositiveOrZero(message = "page must be positive or zero")
            @RequestParam int page,
            @PositiveOrZero(message = "page size must be positive or zero")
            @RequestParam int pageSize
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        return budgetPlanService.getAllBudgetPlans(userId, pageable);
    }

    @PostMapping
    public BudgetPlanDTO createBudgetPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BudgetPlanCreateRequestDTO budgetPlanCreateRequestDTO
    ) {
        validateDate(budgetPlanCreateRequestDTO.getMonth());
        return budgetPlanService.createBudgetPlan(
                BudgetPlanCreateDTO.builder()
                        .userId(userService.getUserByUsername(userDetails.getUsername()).getId())
                        .categoryId(budgetPlanCreateRequestDTO.getCategoryId())
                        .limitAmount(budgetPlanCreateRequestDTO.getLimitAmount())
                        .spentAmount(0.0)
                        .month(budgetPlanCreateRequestDTO.getMonth())
                        .build()
        );
    }

    @PatchMapping
    public BudgetPlanDTO updateBudgetPlanLimit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BudgetPlanUpdateRequestDTO budgetPlanUpdateRequestDTO
    ) {
        return budgetPlanService.updateBudgetPlan(
                BudgetPlanUpdateDTO.builder()
                        .id(budgetPlanUpdateRequestDTO.getId())
                        .userId(userService.getUserByUsername(userDetails.getUsername()).getId())
                        .limitAmount(budgetPlanUpdateRequestDTO.getLimitAmount())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public BudgetPlanDTO deleteBudgetPlan(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        return budgetPlanService.deleteBudgetPlan(id, userId);
    }

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BadRequest("Date cannot be before today");
        }
    }
}
