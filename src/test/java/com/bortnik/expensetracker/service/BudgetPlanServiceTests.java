package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.budget.BudgetPlanCreateDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanUpdateDTO;
import com.bortnik.expensetracker.entities.BudgetPlan;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanAlreadyExists;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.repository.BudgetPlanRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BudgetPlanServiceTests {

    private final BudgetPlanRepository budgetPlanRepository = mock(BudgetPlanRepository.class);
    private final BudgetPlanService budgetPlanService = new BudgetPlanService(budgetPlanRepository);

    private final UUID userId = UUID.randomUUID();
    private final UUID categoryId = UUID.randomUUID();
    private final UUID budgetPlanId = UUID.randomUUID();
    private final LocalDate month = LocalDate.of(2024, 1, 15);
    private final LocalDate startOfMonth = LocalDate.of(2024, 1, 1);
    private final LocalDate endOfMonth = LocalDate.of(2024, 1, 31);
    private final Double limitAmount = 1000.0;
    private final Double spentAmount = 500.0;
    private final Double updatedLimitAmount = 1200.0;
    private final Double updatedSpentAmount = 600.0;

    private final BudgetPlan budgetPlan = BudgetPlan.builder()
            .id(budgetPlanId)
            .userId(userId)
            .categoryId(categoryId)
            .limitAmount(limitAmount)
            .spentAmount(spentAmount)
            .month(month)
            .build();

    private final BudgetPlanDTO budgetPlanDTO = BudgetPlanDTO.builder()
            .id(budgetPlanId)
            .userId(userId)
            .categoryId(categoryId)
            .limitAmount(limitAmount)
            .spentAmount(spentAmount)
            .month(month)
            .build();

    private final BudgetPlanCreateDTO budgetPlanCreateDTO = BudgetPlanCreateDTO.builder()
            .userId(userId)
            .categoryId(categoryId)
            .limitAmount(limitAmount)
            .spentAmount(spentAmount)
            .month(month)
            .build();

    private final BudgetPlanUpdateDTO budgetPlanUpdateDTO = BudgetPlanUpdateDTO.builder()
            .id(budgetPlanId)
            .userId(userId)
            .limitAmount(updatedLimitAmount)
            .spentAmount(updatedSpentAmount)
            .build();

    @Test
    void getBudgetPlanByUserIdAndCategoryIdAndMonth_ShouldReturnBudgetPlan() {
        when(budgetPlanRepository.findByUserIdAndCategoryIdAndMonthBetween(userId, categoryId, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(budgetPlan));

        final BudgetPlanDTO result = budgetPlanService.getBudgetPlanByUserIdAndCategoryIdAndMonth(userId, categoryId, month);

        assertEquals(budgetPlanDTO, result);
    }

    @Test
    void getBudgetPlanByUserIdAndCategoryIdAndMonth_ShouldThrowException_WhenBudgetPlanNotFound() {
        when(budgetPlanRepository.findByUserIdAndCategoryIdAndMonthBetween(userId, categoryId, startOfMonth, endOfMonth))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(BudgetPlanNotFound.class,
                () -> budgetPlanService.getBudgetPlanByUserIdAndCategoryIdAndMonth(userId, categoryId, month));

        assertEquals("Budget plan for category with id " + categoryId +
                " and month " + month + " does not exist", exception.getMessage());
    }

    @Test
    void getBudgetPlanByUserIdAndMonth_ShouldReturnBudgetPlan() {
        when(budgetPlanRepository.findByUserIdAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(budgetPlan));

        final BudgetPlanDTO result = budgetPlanService.getBudgetPlanByUserIdAndMonth(userId, month);

        assertEquals(budgetPlanDTO, result);
    }

    @Test
    void getBudgetPlanByUserIdAndMonth_ShouldThrowException_WhenBudgetPlanNotFound() {
        when(budgetPlanRepository.findByUserIdAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(BudgetPlanNotFound.class,
                () -> budgetPlanService.getBudgetPlanByUserIdAndMonth(userId, month));

        assertEquals("Budget plan for user with id " + userId +
                " and month " + month + " does not exist", exception.getMessage());
    }

    @Test
    void saveBudgetPlan_ShouldCreateBudgetPlan() {
        when(budgetPlanRepository.existsByUserIdAndMonthBetween(userId, month, month)).thenReturn(false);
        when(budgetPlanRepository.save(any(BudgetPlan.class))).thenReturn(budgetPlan);

        final BudgetPlanDTO result = budgetPlanService.saveBudgetPlan(budgetPlanCreateDTO);

        assertEquals(budgetPlanDTO, result);
        verify(budgetPlanRepository, times(1)).save(any(BudgetPlan.class));
    }

    @Test
    void saveBudgetPlan_ShouldThrowException_WhenBudgetPlanAlreadyExists() {
        when(budgetPlanRepository.existsByUserIdAndMonthBetween(userId, month, month)).thenReturn(true);

        final var exception = assertThrows(BudgetPlanAlreadyExists.class,
                () -> budgetPlanService.saveBudgetPlan(budgetPlanCreateDTO));

        assertEquals("the budget plan for this month already exists", exception.getMessage());
    }

    @Test
    void updateBudgetPlan_ShouldUpdateBudgetPlan() {
        final BudgetPlan updatedBudgetPlan = BudgetPlan.builder()
                .id(budgetPlanId)
                .userId(userId)
                .categoryId(categoryId)
                .limitAmount(updatedLimitAmount)
                .spentAmount(updatedSpentAmount)
                .month(month)
                .build();

        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.of(budgetPlan));
        when(budgetPlanRepository.save(any(BudgetPlan.class))).thenReturn(updatedBudgetPlan);

        final BudgetPlanDTO result = budgetPlanService.updateBudgetPlan(budgetPlanUpdateDTO);

        assertEquals(updatedLimitAmount, result.getLimitAmount());
        assertEquals(updatedSpentAmount, result.getSpentAmount());
        verify(budgetPlanRepository).save(budgetPlan);
    }

    @Test
    void updateBudgetPlan_ShouldThrowException_WhenBudgetPlanNotFound() {
        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.empty());

        final var exception = assertThrows(BudgetPlanNotFound.class,
                () -> budgetPlanService.updateBudgetPlan(budgetPlanUpdateDTO));

        assertEquals("Budget plan with id " + budgetPlanId + " does not exist", exception.getMessage());
    }

    @Test
    void updateBudgetPlan_ShouldThrowAccessError_WhenUserDoesNotOwnBudgetPlan() {
        final UUID otherUserId = UUID.randomUUID();
        final BudgetPlan budgetPlanOwnedByOtherUser = BudgetPlan.builder()
                .id(budgetPlanId)
                .userId(otherUserId)
                .categoryId(categoryId)
                .limitAmount(limitAmount)
                .spentAmount(spentAmount)
                .month(month)
                .build();

        final BudgetPlanUpdateDTO updateDTOWithWrongUser = BudgetPlanUpdateDTO.builder()
                .id(budgetPlanId)
                .userId(userId)
                .limitAmount(updatedLimitAmount)
                .spentAmount(updatedSpentAmount)
                .build();

        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.of(budgetPlanOwnedByOtherUser));

        final var exception = assertThrows(AccessError.class,
                () -> budgetPlanService.updateBudgetPlan(updateDTOWithWrongUser));

        assertEquals("You do not have access to this budget plan", exception.getMessage());
        verify(budgetPlanRepository, never()).save(any(BudgetPlan.class));
    }

    @Test
    void deleteBudgetPlan_ShouldDeleteBudgetPlanAndReturnDTO() {
        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.of(budgetPlan));

        final BudgetPlanDTO result = budgetPlanService.deleteBudgetPlan(budgetPlanId, userId);

        assertEquals(budgetPlanDTO, result);
        verify(budgetPlanRepository).deleteById(budgetPlanId);
    }

    @Test
    void deleteBudgetPlan_ShouldThrowException_WhenBudgetPlanNotFound() {
        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.empty());

        final var exception = assertThrows(BudgetPlanNotFound.class,
                () -> budgetPlanService.deleteBudgetPlan(budgetPlanId, userId));

        assertEquals("Budget plan with id " + budgetPlanId + " does not exist", exception.getMessage());
    }

    @Test
    void deleteBudgetPlan_ShouldThrowAccessError_WhenUserDoesNotOwnBudgetPlan() {
        final UUID otherUserId = UUID.randomUUID();
        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.of(budgetPlan));

        final var exception = assertThrows(AccessError.class,
                () -> budgetPlanService.deleteBudgetPlan(budgetPlanId, otherUserId));

        assertEquals("You do not have access to this budget plan", exception.getMessage());
    }
}