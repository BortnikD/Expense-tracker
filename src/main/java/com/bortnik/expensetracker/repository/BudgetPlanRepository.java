package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, UUID> {

    Optional<BudgetPlan> findByUserIdAndCategoryIdAndMonthBetween(
            UUID userId,
            UUID categoryId,
            LocalDate startMonth,
            LocalDate endMonth);

    Optional<BudgetPlan> findByUserIdAndMonthBetween(UUID userId, LocalDate startMonth, LocalDate endMonth);

    boolean existsByUserIdAndMonthBetween(UUID userId, LocalDate startMonth, LocalDate endMonth);
}
