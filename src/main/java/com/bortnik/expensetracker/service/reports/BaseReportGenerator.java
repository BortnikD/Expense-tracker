package com.bortnik.expensetracker.service.reports;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetRowData;
import com.bortnik.expensetracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BaseReportGenerator {

    private final CategoryService categoryService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String WITHOUT_CATEGORY = "Without category";

    public static final String[] BUDGET_HEADERS = {"Month", "Category", "Limit", "Spent", "Remaining", "%"};
    public static final String[] EXPENSE_HEADERS = {"Date", "Category", "Amount", "Description"};

    public BudgetRowData calculateBudgetData(BudgetPlanDTO plan) {
        double limit = plan.getLimitAmount();
        double spent = plan.getSpentAmount();
        double remaining = limit - spent;
        double percentage = limit > 0 ? (spent / limit) * 100 : 0;
        boolean isOverBudget = spent > limit;

        return new BudgetRowData(limit, spent, remaining, percentage / 100, isOverBudget);
    }

    public String getCategoryName(UUID categoryId) {
        if (categoryId == null) {
            return WITHOUT_CATEGORY;
        }
        return categoryService.getCategory(categoryId).getName();
    }

    public String formatMonth(LocalDate month) {
        return month.getMonth().toString() + " " + month.getYear();
    }

    public String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}