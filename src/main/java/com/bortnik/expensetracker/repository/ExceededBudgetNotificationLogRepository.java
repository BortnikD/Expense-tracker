package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.ExceededBudgetNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExceededBudgetNotificationLogRepository extends JpaRepository<ExceededBudgetNotificationLog, UUID> {

    boolean existsByUserIdAndBudgetMonthAndCategoryId(UUID userId, String budgetMonth, UUID categoryId);
}