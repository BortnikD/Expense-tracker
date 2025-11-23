package com.bortnik.expensetracker.util.mappers;

import com.bortnik.expensetracker.dto.budget.BudgetPlanCreateDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.entities.BudgetPlan;

public class BudgetPlanMapper {
    public static BudgetPlanDTO toDto(final BudgetPlan budgetPlan) {
        return BudgetPlanDTO.builder()
                .id(budgetPlan.getId())
                .userId(budgetPlan.getUserId())
                .categoryId(budgetPlan.getCategoryId())
                .limitAmount(budgetPlan.getLimitAmount())
                .spentAmount(budgetPlan.getSpentAmount())
                .month(budgetPlan.getMonth())
                .createdAt(budgetPlan.getCreatedAt())
                .updatedAt(budgetPlan.getUpdatedAt())
                .build();
    }

    public static BudgetPlan toEntity(final BudgetPlanCreateDTO budgetPlanCreateDTO) {
        return BudgetPlan.builder()
                .userId(budgetPlanCreateDTO.getUserId())
                .categoryId(budgetPlanCreateDTO.getCategoryId())
                .limitAmount(budgetPlanCreateDTO.getLimitAmount())
                .spentAmount(budgetPlanCreateDTO.getSpentAmount())
                .month(budgetPlanCreateDTO.getMonth())
                .build();
    }
}
