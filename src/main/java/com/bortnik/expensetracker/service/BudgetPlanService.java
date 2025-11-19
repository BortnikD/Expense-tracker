package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.budget.BudgetPlanCreateDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanUpdateDTO;
import com.bortnik.expensetracker.dto.budget.BudgetUpdateExpenses;
import com.bortnik.expensetracker.entities.BudgetPlan;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanNotFound;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanAlreadyExists;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.util.mappers.BudgetPlanMapper;
import com.bortnik.expensetracker.repository.BudgetPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
                budgetPlanRepository.findByUserIdAndCategoryIdIsNullAndMonthBetween(userId, startMonth, endMonth)
                        .orElseThrow(() -> new BudgetPlanNotFound("Budget plan for user with id " + userId +
                                " and month " + month + " does not exist"))
        );
    }

    public Optional<BudgetPlanDTO> getOptionalBudgetPlanByUserIdAndCategoryIdAndMonth(
            UUID userId,
            UUID categoryId,
            LocalDate month
    ) {
        LocalDate startMonth = month.withDayOfMonth(1);
        LocalDate endMonth = month.withDayOfMonth(month.lengthOfMonth());

        return budgetPlanRepository.findByUserIdAndCategoryIdAndMonthBetween(userId, categoryId, startMonth, endMonth)
                .map(BudgetPlanMapper::toDto);
    }

    public Optional<BudgetPlanDTO> getOptionalBudgetPlanByUserIdAndMonth(
            UUID userId,
            LocalDate month
    ) {
        LocalDate startMonth = month.withDayOfMonth(1);
        LocalDate endMonth = month.withDayOfMonth(month.lengthOfMonth());

        return budgetPlanRepository.findByUserIdAndCategoryIdIsNullAndMonthBetween(userId, startMonth, endMonth)
                .map(BudgetPlanMapper::toDto);
    }

    public List<BudgetPlanDTO> getExceedingBudgetPlans(final UUID userId) {
        return budgetPlanRepository.findByUserIdAndSpentAmountExceedsLimit(userId)
                .stream()
                .map(BudgetPlanMapper::toDto)
                .toList();
    }

    public Page<BudgetPlanDTO> getAllBySpentAmountExceedsLimit(Pageable pageable) {
        LocalDate now = LocalDate.now();
        LocalDate startMonth = now.withDayOfMonth(1);
        LocalDate endMonth = now.withDayOfMonth(now.lengthOfMonth());
        return budgetPlanRepository.findAllBySpentAmountExceedsLimitAndMonthBetween(pageable, startMonth, endMonth)
                .map(BudgetPlanMapper::toDto);
    }

    public List<BudgetPlanDTO> getBudgetPlansByUserIdAndMonth(final UUID userId, final LocalDate month) {
        LocalDate startMonth = month.withDayOfMonth(1);
        LocalDate endMonth = month.withDayOfMonth(month.lengthOfMonth());
        return budgetPlanRepository.findByUserIdAndMonthBetween(userId, startMonth, endMonth)
                .stream()
                .map(BudgetPlanMapper::toDto)
                .toList();
    }

    public Page<BudgetPlanDTO> getAllBudgetPlans(final UUID userId, Pageable pageable) {
        return budgetPlanRepository.findByUserId(userId, pageable).map(BudgetPlanMapper::toDto);
    }

    @Transactional
    public BudgetPlanDTO createBudgetPlan(final BudgetPlanCreateDTO budgetPlanCreateDTO) {
        final LocalDate monthStart = budgetPlanCreateDTO.getMonth().withDayOfMonth(1);
        final LocalDate monthEnd = budgetPlanCreateDTO.getMonth().withDayOfMonth(
                budgetPlanCreateDTO.getMonth().lengthOfMonth());

        boolean alreadyExists;

        if (budgetPlanCreateDTO.getCategoryId() == null) {
            alreadyExists = budgetPlanRepository.existsByUserIdAndCategoryIdIsNullAndMonthBetween(
                    budgetPlanCreateDTO.getUserId(), monthStart, monthEnd
            );
        } else {
            alreadyExists = budgetPlanRepository.existsByUserIdAndCategoryIdAndMonthBetween(
                    budgetPlanCreateDTO.getUserId(), budgetPlanCreateDTO.getCategoryId(), monthStart, monthEnd
            );
        }

        if (alreadyExists) {
            throw new BudgetPlanAlreadyExists("The budget plan for this month and category already exists");
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
        return BudgetPlanMapper.toDto(budgetPlanRepository.save(budgetPlan));
    }

    /**
     * Updates the spent amount in the budget plan by appending additional expenses.
     * Retrieves the budget plan based on the identifier provided and updates the spent amount
     * by adding the value of the expenses. If the budget plan does not exist, an exception is thrown.
     *
     * @param budgetUpdateExpenses an object containing the ID of the budget plan to update and
     *                             the amount of expenses to append
     * @throws BudgetPlanNotFound if the budget plan with the specified ID does not exist
     */
    @Transactional
    public void appendExpensesToBudgetPlan(final BudgetUpdateExpenses budgetUpdateExpenses) {
        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetUpdateExpenses.getId())
                .orElseThrow(() -> new BudgetPlanNotFound("Budget plan with id " + budgetUpdateExpenses.getId() +
                        " does not exist"));

        budgetPlan.setSpentAmount(budgetPlan.getSpentAmount() + budgetUpdateExpenses.getSpentAmount());

        BudgetPlanMapper.toDto(budgetPlanRepository.save(budgetPlan));
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
