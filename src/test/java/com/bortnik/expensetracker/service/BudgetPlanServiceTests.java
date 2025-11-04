package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.budget.BudgetPlanCreateDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetPlanUpdateDTO;
import com.bortnik.expensetracker.dto.budget.BudgetUpdateExpenses;
import com.bortnik.expensetracker.entities.BudgetPlan;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanAlreadyExists;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.repository.BudgetPlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
        when(budgetPlanRepository.findByUserIdAndCategoryIdIsNullAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(budgetPlan));

        final BudgetPlanDTO result = budgetPlanService.getBudgetPlanByUserIdAndMonth(userId, month);

        assertEquals(budgetPlanDTO, result);
    }

    @Test
    void getBudgetPlanByUserIdAndMonth_ShouldThrowException_WhenBudgetPlanNotFound() {
        when(budgetPlanRepository.findByUserIdAndCategoryIdIsNullAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(BudgetPlanNotFound.class,
                () -> budgetPlanService.getBudgetPlanByUserIdAndMonth(userId, month));

        assertEquals("Budget plan for user with id " + userId +
                " and month " + month + " does not exist", exception.getMessage());
    }

    @Test
    void getOptionalBudgetPlanByUserIdAndCategoryIdAndMonth_ShouldReturnOptionalWithBudgetPlan() {
        when(budgetPlanRepository.findByUserIdAndCategoryIdAndMonthBetween(userId, categoryId, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(budgetPlan));

        final Optional<BudgetPlanDTO> result = budgetPlanService.getOptionalBudgetPlanByUserIdAndCategoryIdAndMonth(userId, categoryId, month);

        assertTrue(result.isPresent());
        assertEquals(budgetPlanDTO, result.get());
    }

    @Test
    void getOptionalBudgetPlanByUserIdAndCategoryIdAndMonth_ShouldReturnEmptyOptional_WhenBudgetPlanNotFound() {
        when(budgetPlanRepository.findByUserIdAndCategoryIdAndMonthBetween(userId, categoryId, startOfMonth, endOfMonth))
                .thenReturn(Optional.empty());

        final Optional<BudgetPlanDTO> result = budgetPlanService.getOptionalBudgetPlanByUserIdAndCategoryIdAndMonth(userId, categoryId, month);

        assertTrue(result.isEmpty());
    }

    @Test
    void getOptionalBudgetPlanByUserIdAndMonth_ShouldReturnOptionalWithBudgetPlan() {
        when(budgetPlanRepository.findByUserIdAndCategoryIdIsNullAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(budgetPlan));

        final Optional<BudgetPlanDTO> result = budgetPlanService.getOptionalBudgetPlanByUserIdAndMonth(userId, month);

        assertTrue(result.isPresent());
        assertEquals(budgetPlanDTO, result.get());
    }

    @Test
    void getOptionalBudgetPlanByUserIdAndMonth_ShouldReturnEmptyOptional_WhenBudgetPlanNotFound() {
        when(budgetPlanRepository.findByUserIdAndCategoryIdIsNullAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(Optional.empty());

        final Optional<BudgetPlanDTO> result = budgetPlanService.getOptionalBudgetPlanByUserIdAndMonth(userId, month);

        assertTrue(result.isEmpty());
    }

    @Test
    void getExceedingBudgetPlans_ShouldReturnListOfExceedingBudgetPlans() {
        final BudgetPlan exceedingBudgetPlan1 = BudgetPlan.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .categoryId(categoryId)
                .limitAmount(800.0)
                .spentAmount(1000.0)
                .month(month)
                .build();

        final BudgetPlan exceedingBudgetPlan2 = BudgetPlan.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .categoryId(UUID.randomUUID())
                .limitAmount(600.0)
                .spentAmount(700.0)
                .month(month)
                .build();

        final List<BudgetPlan> exceedingPlans = Arrays.asList(exceedingBudgetPlan1, exceedingBudgetPlan2);

        when(budgetPlanRepository.findByUserIdAndSpentAmountExceedsLimit(userId))
                .thenReturn(exceedingPlans);

        final List<BudgetPlanDTO> result = budgetPlanService.getExceedingBudgetPlans(userId);

        assertEquals(2, result.size());
        assertEquals(1000.0, result.get(0).getSpentAmount());
        assertEquals(700.0, result.get(1).getSpentAmount());
        verify(budgetPlanRepository).findByUserIdAndSpentAmountExceedsLimit(userId);
    }

    @Test
    void getExceedingBudgetPlans_ShouldReturnEmptyList_WhenNoBudgetPlansExceedLimit() {
        when(budgetPlanRepository.findByUserIdAndSpentAmountExceedsLimit(userId))
                .thenReturn(List.of());

        final List<BudgetPlanDTO> result = budgetPlanService.getExceedingBudgetPlans(userId);

        assertTrue(result.isEmpty());
        verify(budgetPlanRepository).findByUserIdAndSpentAmountExceedsLimit(userId);
    }

    @Test
    void getBudgetPlansByUserIdAndMonth_ShouldReturnListOfBudgetPlans() {
        final BudgetPlan budgetPlan2 = BudgetPlan.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .categoryId(UUID.randomUUID())
                .limitAmount(800.0)
                .spentAmount(400.0)
                .month(month)
                .build();

        final List<BudgetPlan> budgetPlans = Arrays.asList(budgetPlan, budgetPlan2);

        when(budgetPlanRepository.findByUserIdAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(budgetPlans);

        final List<BudgetPlanDTO> result = budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month);

        assertEquals(2, result.size());
        assertEquals(budgetPlan.getId(), result.get(0).getId());
        assertEquals(budgetPlan2.getId(), result.get(1).getId());
        verify(budgetPlanRepository).findByUserIdAndMonthBetween(userId, startOfMonth, endOfMonth);
    }

    @Test
    void getBudgetPlansByUserIdAndMonth_ShouldReturnEmptyList_WhenNoBudgetPlansFound() {
        when(budgetPlanRepository.findByUserIdAndMonthBetween(userId, startOfMonth, endOfMonth))
                .thenReturn(List.of());

        final List<BudgetPlanDTO> result = budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month);

        assertTrue(result.isEmpty());
        verify(budgetPlanRepository).findByUserIdAndMonthBetween(userId, startOfMonth, endOfMonth);
    }

    @Test
    void getAllBudgetPlans_ShouldReturnPageOfBudgetPlans() {
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<BudgetPlan> budgetPlanPage = new PageImpl<>(Collections.singletonList(budgetPlan), pageable, 1);

        when(budgetPlanRepository.findByUserId(userId, pageable))
                .thenReturn(budgetPlanPage);

        final Page<BudgetPlanDTO> result = budgetPlanService.getAllBudgetPlans(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(budgetPlanDTO, result.getContent().getFirst());
        verify(budgetPlanRepository).findByUserId(userId, pageable);
    }

    @Test
    void getAllBudgetPlans_ShouldReturnEmptyPage_WhenNoBudgetPlansFound() {
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<BudgetPlan> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(budgetPlanRepository.findByUserId(userId, pageable))
                .thenReturn(emptyPage);

        final Page<BudgetPlanDTO> result = budgetPlanService.getAllBudgetPlans(userId, pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(budgetPlanRepository).findByUserId(userId, pageable);
    }

    @Test
    void createBudgetPlan_ShouldCreateBudgetPlan() {
        when(budgetPlanRepository.existsByUserIdAndMonthBetween(userId, month, month)).thenReturn(false);
        when(budgetPlanRepository.save(any(BudgetPlan.class))).thenReturn(budgetPlan);

        final BudgetPlanDTO result = budgetPlanService.createBudgetPlan(budgetPlanCreateDTO);

        assertEquals(budgetPlanDTO, result);
        verify(budgetPlanRepository, times(1)).save(any(BudgetPlan.class));
    }

    @Test
    void createBudgetPlan_ShouldThrowException_WhenBudgetPlanAlreadyExists() {
        when(budgetPlanRepository.existsByUserIdAndMonthBetween(userId, month, month)).thenReturn(true);

        final var exception = assertThrows(BudgetPlanAlreadyExists.class,
                () -> budgetPlanService.createBudgetPlan(budgetPlanCreateDTO));

        assertEquals("the budget plan for this month already exists", exception.getMessage());
    }

    @Test
    void updateBudgetPlan_ShouldUpdateBudgetPlan() {
        final BudgetPlan updatedBudgetPlan = BudgetPlan.builder()
                .id(budgetPlanId)
                .userId(userId)
                .categoryId(categoryId)
                .limitAmount(updatedLimitAmount)
                .spentAmount(spentAmount)
                .month(month)
                .build();

        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.of(budgetPlan));
        when(budgetPlanRepository.save(any(BudgetPlan.class))).thenReturn(updatedBudgetPlan);

        final BudgetPlanDTO result = budgetPlanService.updateBudgetPlan(budgetPlanUpdateDTO);

        assertEquals(updatedLimitAmount, result.getLimitAmount());
        assertEquals(spentAmount, result.getSpentAmount());
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
                .build();

        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.of(budgetPlanOwnedByOtherUser));

        final var exception = assertThrows(AccessError.class,
                () -> budgetPlanService.updateBudgetPlan(updateDTOWithWrongUser));

        assertEquals("You do not have access to this budget plan", exception.getMessage());
        verify(budgetPlanRepository, never()).save(any(BudgetPlan.class));
    }

    @Test
    void appendExpensesToBudgetPlan_ShouldUpdateSpentAmount() {
        final Double additionalExpenses = 150.0;
        final BudgetUpdateExpenses budgetUpdateExpenses = BudgetUpdateExpenses.builder()
                .id(budgetPlanId)
                .spentAmount(additionalExpenses)
                .build();

        final BudgetPlan updatedBudgetPlan = BudgetPlan.builder()
                .id(budgetPlanId)
                .userId(userId)
                .categoryId(categoryId)
                .limitAmount(limitAmount)
                .spentAmount(spentAmount + additionalExpenses)
                .month(month)
                .build();

        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.of(budgetPlan));
        when(budgetPlanRepository.save(any(BudgetPlan.class))).thenReturn(updatedBudgetPlan);

        budgetPlanService.appendExpensesToBudgetPlan(budgetUpdateExpenses);

        assertEquals(spentAmount + additionalExpenses, budgetPlan.getSpentAmount());
        verify(budgetPlanRepository).findById(budgetPlanId);
        verify(budgetPlanRepository).save(budgetPlan);
    }

    @Test
    void appendExpensesToBudgetPlan_ShouldThrowException_WhenBudgetPlanNotFound() {
        final BudgetUpdateExpenses budgetUpdateExpenses = BudgetUpdateExpenses.builder()
                .id(budgetPlanId)
                .spentAmount(150.0)
                .build();

        when(budgetPlanRepository.findById(budgetPlanId)).thenReturn(Optional.empty());

        final var exception = assertThrows(BudgetPlanNotFound.class,
                () -> budgetPlanService.appendExpensesToBudgetPlan(budgetUpdateExpenses));

        assertEquals("Budget plan with id " + budgetPlanId + " does not exist", exception.getMessage());
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
        verify(budgetPlanRepository, never()).deleteById(budgetPlanId);
    }
}