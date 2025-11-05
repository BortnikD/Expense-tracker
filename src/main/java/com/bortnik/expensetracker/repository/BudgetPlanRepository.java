package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.BudgetPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, UUID> {

    Optional<BudgetPlan> findByUserIdAndCategoryIdAndMonthBetween(
            UUID userId,
            UUID categoryId,
            LocalDate startMonth,
            LocalDate endMonth);

    Optional<BudgetPlan> findByUserIdAndCategoryIdIsNullAndMonthBetween(
            UUID userId,
            LocalDate startMonth,
            LocalDate endMonth);

    boolean existsByUserIdAndCategoryIdIsNullAndMonthBetween(UUID userId, LocalDate startMonth, LocalDate endMonth);

    boolean existsByUserIdAndCategoryIdAndMonthBetween(UUID userId, UUID categoryId, LocalDate startMonth, LocalDate endMonth);

    @Query("""
    SELECT b FROM BudgetPlan b
    WHERE b.userId = :userId
    AND b.spentAmount > b.limitAmount
""")
    List<BudgetPlan> findByUserIdAndSpentAmountExceedsLimit(UUID userId);

    @Query("""
    SELECT b FROM BudgetPlan b
    WHERE b.spentAmount > b.limitAmount
    AND b.month BETWEEN :startMonth AND :endMonth
""")
    Page<BudgetPlan> findAllBySpentAmountExceedsLimitAndMonthBetween(Pageable pageable, LocalDate startMonth, LocalDate endMonth);

    List<BudgetPlan> findByUserIdAndMonthBetween(
            UUID userId,
            LocalDate startMonth,
            LocalDate endMonth);

    Page<BudgetPlan> findByUserId(UUID userId, Pageable pageable);
}
