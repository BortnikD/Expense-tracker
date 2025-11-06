package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.ExceededBudgetNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ExceededBudgetNotificationLogRepository extends JpaRepository<ExceededBudgetNotificationLog, UUID> {

    boolean existsByUserIdAndBudgetMonthAndCategoryId(UUID userId, String budgetMonth, UUID categoryId);

    void deleteAllByNotifiedAtBefore(OffsetDateTime month);
}
