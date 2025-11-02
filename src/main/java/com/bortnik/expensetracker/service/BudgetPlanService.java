package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.budget.BudgetPlanCreateDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanUpdateDTO;
import com.bortnik.expensetracker.entities.BudgetPlan;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanNotFound;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanAlreadyExists;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.mappers.BudgetPlanMapper;
import com.bortnik.expensetracker.repository.BudgetPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetPlanService {
    private final BudgetPlanRepository budgetPlanRepository;

    public BudgetPlanDTO getBudgetPlanByUserIdAndCategoryIdAndMonth(
            final UUID userId,
            final UUID categoryId,
            final LocalDate month
    ) {
        final LocalDate startMonth = month.withDayOfMonth(1);
        final LocalDate endMonth = month.withDayOfMonth(month.lengthOfMonth());
        return BudgetPlanMapper.toDto(
                budgetPlanRepository.findByUserIdAndCategoryIdAndMonthBetween(userId, categoryId, startMonth, endMonth)
                        .orElseThrow(() -> new BudgetPlanNotFound("Budget plan for category with id " + categoryId +
                                " and month " + month + " does not exist"))
        );
    }

    public BudgetPlanDTO getBudgetPlanByUserIdAndMonth(
            final UUID userId,
            final LocalDate month
    ) {
        final LocalDate startMonth = month.withDayOfMonth(1);
        final LocalDate endMonth = month.withDayOfMonth(month.lengthOfMonth());
        return BudgetPlanMapper.toDto(
                budgetPlanRepository.findByUserIdAndMonthBetween(userId, startMonth, endMonth)
                        .orElseThrow(() -> new BudgetPlanNotFound("Budget plan for user with id " + userId +
                                " and month " + month + " does not exist"))
        );
    }

    @Transactional
    public BudgetPlanDTO saveBudgetPlan(final BudgetPlanCreateDTO budgetPlanCreateDTO) {
        if (budgetPlanRepository.existsByUserIdAndMonthBetween(
                budgetPlanCreateDTO.getUserId(), budgetPlanCreateDTO.getMonth(), budgetPlanCreateDTO.getMonth())
        ) {
            throw new BudgetPlanAlreadyExists("the budget plan for this month already exists");
        }
        return BudgetPlanMapper.toDto(
                budgetPlanRepository.save(BudgetPlanMapper.toEntity(budgetPlanCreateDTO))
        );
    }

    @Transactional
    public BudgetPlanDTO updateBudgetPlan(final BudgetPlanUpdateDTO budgetPlanUpdateDTO) {
        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetPlanUpdateDTO.getId())
                .orElseThrow(() ->
                        new BudgetPlanNotFound("Budget plan with id " + budgetPlanUpdateDTO.getId() + " does not exist"));

        if (!budgetPlan.getUserId().equals(budgetPlanUpdateDTO.getUserId())) {
            throw new AccessError("You do not have access to this budget plan");
        }

        budgetPlan.setLimitAmount(budgetPlanUpdateDTO.getLimitAmount());
        budgetPlan.setSpentAmount(budgetPlanUpdateDTO.getSpentAmount());
        return BudgetPlanMapper.toDto(budgetPlanRepository.save(budgetPlan));
    }

    /**
     * Deletes an existing budget plan identified by its ID and verifies that the user has access
     * to the specified budget plan.
     *
     * @param id the unique identifier of the budget plan to be deleted
     * @param userId the unique identifier of the user requesting the deletion
     * @return the deleted budget plan details as a {@link BudgetPlanDTO} object
     * @throws BudgetPlanNotFound if the budget plan with the specified ID does not exist
     * @throws AccessError if the user does not have access to the specified budget plan
     */
    @Transactional
    public BudgetPlanDTO deleteBudgetPlan(final UUID id, final UUID userId) {
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() ->
                        new BudgetPlanNotFound("Budget plan with id " + id + " does not exist"));

        if (!budgetPlan.getUserId().equals(userId)) {
            throw new AccessError("You do not have access to this budget plan");
        }

        budgetPlanRepository.deleteById(id);
        return BudgetPlanMapper.toDto(budgetPlan);
    }
}
