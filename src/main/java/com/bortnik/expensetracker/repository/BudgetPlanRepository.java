package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, UUID> {

    BudgetPlan findByUserIdAndCategoryIdAndMonth(UUID userId, UUID categoryId, LocalDate month);

    BudgetPlan findByUserIdAndMonth(UUID userId, LocalDate month);
}
