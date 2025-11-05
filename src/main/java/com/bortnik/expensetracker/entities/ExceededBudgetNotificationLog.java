package com.bortnik.expensetracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exceeded_budget_notification_log",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "budget_month", "category_id"}))
public class ExceededBudgetNotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "budget_month", nullable = false)
    private String budgetMonth; // e.g., "2025-11"

    @Column(name = "category_id") // Can be null
    private UUID categoryId;

    @Column(name = "notified_at", nullable = false)
    private OffsetDateTime notifiedAt;
}