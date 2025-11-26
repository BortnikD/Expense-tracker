//package com.bortnik.expensetracker.service;
//
//import com.bortnik.expensetracker.dto.expenses.ExpensesCreateDTO;
//import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
//import com.bortnik.expensetracker.dto.expenses.ExpensesUpdateDTO;
//import com.bortnik.expensetracker.entities.Expenses;
//import com.bortnik.expensetracker.exceptions.expenses.ExpensesNotFound;
//import com.bortnik.expensetracker.exceptions.user.AccessError;
//import com.bortnik.expensetracker.mappers.ExpensesMapper;
//import com.bortnik.expensetracker.repository.ExpensesRepository;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class ExpensesServiceTests {
//
//    private final ExpensesRepository expensesRepository = mock(ExpensesRepository.class);
//    private final BudgetPlanService budgetPlanService = mock(BudgetPlanService.class);
//    private final ExpensesService expensesService = new ExpensesService(expensesRepository, budgetPlanService);
//
//    private final UUID expenseId = UUID.randomUUID();
//    private final UUID userId = UUID.randomUUID();
//    private final UUID otherUserId = UUID.randomUUID();
//    private final UUID categoryId = UUID.randomUUID();
//    private final LocalDate date = LocalDate.of(2025, 1, 1);
//
//    private final Expenses sampleExpenses = Expenses.builder()
//            .id(expenseId)
//            .userId(userId)
//            .categoryId(categoryId)
//            .amount(100.0)
//            .date(date)
//            .description("sample")
//            .build();
//
//    @Test
//    void createExpenses_ShouldSaveAndReturnDto() {
//        // given
//        ExpensesCreateDTO createDTO = ExpensesCreateDTO.builder()
//                .userId(userId)
//                .categoryId(categoryId)
//                .amount(100.0)
//                .date(date)
//                .description("sample")
//                .build();
//
//        when(expensesRepository.save(any(Expenses.class))).thenReturn(sampleExpenses);
//
//        // when
//        ExpensesDTO result = expensesService.createExpenses(createDTO);
//
//        // then
//        ExpensesDTO expected = ExpensesMapper.toDto(sampleExpenses);
//        assertEquals(expected, result);
//        verify(expensesRepository, times(1)).save(any(Expenses.class));
//    }
//
//    @Test
//    void getExpensesBetweenDates_ShouldReturnMappedList() {
//        // given
//        LocalDate start = date.minusDays(1);
//        LocalDate end = date.plusDays(1);
//        when(expensesRepository.findByUserIdAndDateBetween(userId, start, end))
//                .thenReturn(List.of(sampleExpenses));
//
//        // when
//        List<ExpensesDTO> result = expensesService.getExpensesBetweenDates(userId, start, end);
//
//        // then
//        List<ExpensesDTO> expected = ExpensesMapper.toDtoList(List.of(sampleExpenses));
//        assertEquals(expected, result);
//        verify(expensesRepository, times(1)).findByUserIdAndDateBetween(userId, start, end);
//    }
//
//    @Test
//    void getUserExpensesByCategoryBetweenDates_ShouldReturnMappedList() {
//        // given
//        LocalDate start = date.minusDays(5);
//        LocalDate end = date.plusDays(5);
//        when(expensesRepository.findByUserIdAndCategoryIdAndDateBetween(userId, categoryId, start, end))
//                .thenReturn(List.of(sampleExpenses));
//
//        // when
//        List<ExpensesDTO> result = expensesService.getUserExpensesByCategoryBetweenDates(userId, categoryId, start, end);
//
//        // then
//        List<ExpensesDTO> expected = ExpensesMapper.toDtoList(List.of(sampleExpenses));
//        assertEquals(expected, result);
//        verify(expensesRepository, times(1))
//                .findByUserIdAndCategoryIdAndDateBetween(userId, categoryId, start, end);
//    }
//
//    @Test
//    void getUserExpensesByDate_ShouldReturnMappedList() {
//        // given
//        when(expensesRepository.findByUserIdAndDate(userId, date))
//                .thenReturn(List.of(sampleExpenses));
//
//        // when
//        List<ExpensesDTO> result = expensesService.getUserExpensesByDate(userId, date);
//
//        // then
//        List<ExpensesDTO> expected = ExpensesMapper.toDtoList(List.of(sampleExpenses));
//        assertEquals(expected, result);
//        verify(expensesRepository, times(1)).findByUserIdAndDate(userId, date);
//    }
//
//    @Test
//    void getUserExpensesByDateAndCategory_ShouldReturnMappedList() {
//        // given
//        when(expensesRepository.findByUserIdAndCategoryIdAndDate(userId, categoryId, date))
//                .thenReturn(List.of(sampleExpenses));
//
//        // when
//        List<ExpensesDTO> result = expensesService.getUserExpensesByDateAndCategory(userId, categoryId, date);
//
//        // then
//        List<ExpensesDTO> expected = ExpensesMapper.toDtoList(List.of(sampleExpenses));
//        assertEquals(expected, result);
//        verify(expensesRepository, times(1))
//                .findByUserIdAndCategoryIdAndDate(userId, categoryId, date);
//    }
//
//    @Test
//    void updateExpenses_ShouldUpdateAndReturnDto_WhenExistsAndUserMatches() {
//        // given
//        ExpensesUpdateDTO updateDTO = ExpensesUpdateDTO.builder()
//                .id(expenseId)
//                .userId(userId)
//                .amount(200.0)
//                .date(date.plusDays(1))
//                .description("updated")
//                .build();
//
//        Expenses existing = Expenses.builder()
//                .id(expenseId)
//                .userId(userId)
//                .categoryId(categoryId)
//                .amount(100.0)
//                .date(date)
//                .description("old")
//                .build();
//
//        Expenses saved = Expenses.builder()
//                .id(expenseId)
//                .userId(userId)
//                .categoryId(categoryId)
//                .amount(updateDTO.getAmount())
//                .date(updateDTO.getDate())
//                .description(updateDTO.getDescription())
//                .build();
//
//        when(expensesRepository.findById(expenseId)).thenReturn(Optional.of(existing));
//        when(expensesRepository.save(existing)).thenReturn(saved);
//
//        // when
//        ExpensesDTO result = expensesService.updateExpenses(updateDTO);
//
//        // then
//        ExpensesDTO expected = ExpensesMapper.toDto(saved);
//        assertEquals(expected, result);
//        verify(expensesRepository).findById(expenseId);
//        verify(expensesRepository).save(existing);
//    }
//
//    @Test
//    void updateExpenses_ShouldThrowNotFound_WhenMissing() {
//        // given
//        ExpensesUpdateDTO updateDTO = ExpensesUpdateDTO.builder()
//                .id(expenseId)
//                .userId(userId)
//                .amount(200.0)
//                .date(date)
//                .description("updated")
//                .build();
//
//        when(expensesRepository.findById(expenseId)).thenReturn(Optional.empty());
//
//        // when / then
//        ExpensesNotFound ex = assertThrows(ExpensesNotFound.class,
//                () -> expensesService.updateExpenses(updateDTO));
//        assertEquals("Expenses with id " + expenseId + " does not exist", ex.getMessage());
//
//        verify(expensesRepository).findById(expenseId);
//        verify(expensesRepository, never()).save(any());
//    }
//
//    @Test
//    void updateExpenses_ShouldThrowAccessError_WhenUserMismatch() {
//        // given
//        ExpensesUpdateDTO updateDTO = ExpensesUpdateDTO.builder()
//                .id(expenseId)
//                .userId(otherUserId)
//                .amount(200.0)
//                .date(date)
//                .description("updated")
//                .build();
//
//        Expenses existing = Expenses.builder()
//                .id(expenseId)
//                .userId(userId)
//                .categoryId(categoryId)
//                .amount(100.0)
//                .date(date)
//                .description("old")
//                .build();
//
//        when(expensesRepository.findById(expenseId)).thenReturn(Optional.of(existing));
//
//        // when / then
//        AccessError ex = assertThrows(AccessError.class,
//                () -> expensesService.updateExpenses(updateDTO));
//        assertEquals("You do not have access to this expenses", ex.getMessage());
//
//        verify(expensesRepository).findById(expenseId);
//        verify(expensesRepository, never()).save(any());
//    }
//}
//
