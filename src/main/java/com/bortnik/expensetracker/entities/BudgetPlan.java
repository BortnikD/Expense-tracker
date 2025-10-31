package com.bortnik.expensetracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "budget_plans",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name", "month"})
)
public class BudgetPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "limit_amount", precision = 15, scale = 2, nullable = false)
    private Double limitAmount;

    @Column(name = "spent_amount", precision = 15, scale = 2, nullable = false)
    private Double spentAmount;

    @Column
    private LocalDate month;
}
