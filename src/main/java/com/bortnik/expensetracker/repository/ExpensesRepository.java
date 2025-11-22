package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.Expenses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, UUID> {

    Page<Expenses> findByUserIdAndDateBetween(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    Page<Expenses> findByUserIdAndCategoryIdAndDateBetween(
            UUID userId,
            UUID categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    Page<Expenses> findByUserIdAndDate(UUID userId, LocalDate date, Pageable pageable);

    Page<Expenses> findByUserIdAndCategoryIdAndDate(
            UUID userId,
            UUID categoryId,
            LocalDate date,
            Pageable pageable
    );
}
