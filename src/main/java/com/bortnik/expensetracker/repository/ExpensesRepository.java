package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, UUID> {

    List<Expenses> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);

    List<Expenses> findByUserIdAndCategoryIdAndDateBetween(
            UUID userId, UUID categoryId, LocalDate startDate, LocalDate endDate
    );

    List<Expenses> findByUserIdAndDate(UUID userId, LocalDate date);

    List<Expenses> findByUserIdAndCategoryIdAndDate(UUID userId, UUID categoryId, LocalDate date);
}
