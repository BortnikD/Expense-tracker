package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.expenses.ExpensesCreateDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesUpdateDTO;
import com.bortnik.expensetracker.entities.Expenses;
import com.bortnik.expensetracker.exceptions.expenses.ExpensesNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.mappers.ExpensesMapper;
import com.bortnik.expensetracker.repository.ExpensesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpensesService {
    private final ExpensesRepository expensesRepository;

    @Transactional
    public ExpensesDTO createExpenses(final ExpensesCreateDTO expensesCreateDTO) {
        return ExpensesMapper.toDto(
                expensesRepository.save(
                        ExpensesMapper.toEntity(expensesCreateDTO)
                )
        );
    }

    public List<ExpensesDTO> getExpensesBetweenDates(
            final UUID userId,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        return ExpensesMapper.toDtoList(
                expensesRepository.findByUserIdAndDateBetween(userId, startDate, endDate)
        );
    }

    public List<ExpensesDTO> getUserExpensesByCategoryBetweenDates(
            final UUID userId,
            final UUID categoryId,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        return ExpensesMapper.toDtoList(
                expensesRepository.findByUserIdAndCategoryIdAndDateBetween(userId, categoryId, startDate, endDate)
        );
    }

    public List<ExpensesDTO> getUserExpensesByDate(
            final UUID userId,
            final LocalDate date
    ) {
        return ExpensesMapper.toDtoList(
                expensesRepository.findByUserIdAndDate(userId, date)
        );
    }

    public List<ExpensesDTO> getUserExpensesByDateAndCategory(
            final UUID userId,
            final UUID categoryId,
            final LocalDate date
    ) {
        return ExpensesMapper.toDtoList(
                expensesRepository.findByUserIdAndCategoryIdAndDate(userId, categoryId, date)
        );
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
        expenses.setDate(expensesUpdateDTO.getDate());
        expenses.setDescription(expensesUpdateDTO.getDescription());
        return ExpensesMapper.toDto(expensesRepository.save(expenses));
    }
}
