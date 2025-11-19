package com.bortnik.expensetracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
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

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "budget_month")
    private String budgetMonth; // e.g., "2025-11"

    @Column(name = "category_id") // Can be null
    private UUID categoryId;

    @Setter
    @Column(name = "notified_at")
    private OffsetDateTime notifiedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExceededBudgetNotificationLog that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}