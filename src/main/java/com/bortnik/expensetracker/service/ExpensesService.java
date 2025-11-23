package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetUpdateExpenses;
import com.bortnik.expensetracker.dto.expenses.ExpensesCreateDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesUpdateDTO;
import com.bortnik.expensetracker.entities.Expenses;
import com.bortnik.expensetracker.exceptions.expenses.ExpensesNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.util.mappers.ExpensesMapper;
import com.bortnik.expensetracker.repository.ExpensesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpensesService {
    private final ExpensesRepository expensesRepository;
    private final BudgetPlanService budgetPlanService;

    @Transactional
    public ExpensesDTO createExpenses(final ExpensesCreateDTO expensesCreateDTO) {
        ExpensesDTO expensesDTO = ExpensesMapper.toDto(
                expensesRepository.save(
                        ExpensesMapper.toEntity(expensesCreateDTO)
                )
        );
        updateBudgetPlans(expensesDTO);
        return expensesDTO;
    }

    public Page<ExpensesDTO> getExpensesBetweenDates(
            final UUID userId,
            final LocalDate startDate,
            final LocalDate endDate,
            final Pageable pageable
    ) {
        return expensesRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable)
                .map(ExpensesMapper::toDto);
    }

    public Page<ExpensesDTO> getUserExpensesByCategoryBetweenDates(
            final UUID userId,
            final UUID categoryId,
            final LocalDate startDate,
            final LocalDate endDate,
            final Pageable pageable
    ) {
        return expensesRepository.findByUserIdAndCategoryIdAndDateBetween(
                userId, categoryId, startDate, endDate, pageable
        ).map(ExpensesMapper::toDto);
    }

    public Page<ExpensesDTO> getUserExpensesByDate(
            final UUID userId,
            final LocalDate date,
            final Pageable pageable
    ) {
        return expensesRepository.findByUserIdAndDate(userId, date, pageable)
                .map(ExpensesMapper::toDto);
    }

    public Page<ExpensesDTO> getUserExpensesByDateAndCategory(
            final UUID userId,
            final UUID categoryId,
            final LocalDate date,
            final Pageable pageable
    ) {
        return expensesRepository.findByUserIdAndCategoryIdAndDate(userId, categoryId, date, pageable)
                .map(ExpensesMapper::toDto);
    }

    @Transactional
    public ExpensesDTO updateExpenses(final ExpensesUpdateDTO expensesUpdateDTO) {
        Expenses expenses = expensesRepository.findById(expensesUpdateDTO.getId())
                .orElseThrow(() ->
                        new ExpensesNotFound("Expenses with id " + expensesUpdateDTO.getId() + " does not exist"));

        if (!expenses.getUserId().equals(expensesUpdateDTO.getUserId())) {
            throw new AccessError("You do not have access to this expenses");
        }

        expenses.setAmount(expensesUpdateDTO.getAmount());
        expenses.setDescription(expensesUpdateDTO.getDescription());
        expenses.setUpdatedAt(OffsetDateTime.now());
        return ExpensesMapper.toDto(expensesRepository.save(expenses));
    }

    private void updateBudgetPlans(ExpensesDTO expensesDTO) {
        LocalDate currentMonth = LocalDate.now();

        if (expensesDTO.getCategoryId() != null) {
            budgetPlanService.getOptionalBudgetPlanByUserIdAndCategoryIdAndMonth(
                            expensesDTO.getUserId(), expensesDTO.getCategoryId(), currentMonth
                    )
                    .map(BudgetPlanDTO::getId)
                    .ifPresent(id -> updateBudgetPlan(id, expensesDTO.getAmount(), "Category"));
        }

        budgetPlanService.getOptionalBudgetPlanByUserIdAndMonth(
                        expensesDTO.getUserId(), currentMonth
                )
                .map(BudgetPlanDTO::getId)
                .ifPresent(id -> updateBudgetPlan(id, expensesDTO.getAmount(), "User"));
    }

    private void updateBudgetPlan(UUID id, double amount, String type) {
        budgetPlanService.appendExpensesToBudgetPlan(
                BudgetUpdateExpenses.builder()
                        .id(id)
                        .spentAmount(amount)
                        .build());
        log.info("{} budget plan with id {} updated with spent amount {}", type, id, amount);
    }
}
