package com.bortnik.expensetracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "budget_plans",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "category_id", "month"})
)
public class BudgetPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "category_id")
    private UUID categoryId;

    @Setter
    @Column(name = "limit_amount")
    private Double limitAmount;

    @Setter
    @Column(name = "spent_amount")
    private Double spentAmount;

    @Column
    private LocalDate month;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetPlan that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
