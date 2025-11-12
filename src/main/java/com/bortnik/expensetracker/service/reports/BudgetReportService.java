package com.bortnik.expensetracker.service.reports;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import com.bortnik.expensetracker.service.BudgetPlanService;
import com.bortnik.expensetracker.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetReportService {

    private final ExcelReportGenerator excelReportGenerator;
    private final BudgetPlanService budgetPlanService;
    private final ExpensesService expensesService;

    public byte[] generateBudgetReport(UUID userId, LocalDate month) {
        List<BudgetPlanDTO> budgetPlans = budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month);
        return excelReportGenerator.generateBudgetReport(budgetPlans);
    }

    public byte[] generateExpensesReport(
            UUID userId,
            LocalDate startMonth,
            LocalDate endMonth
    ) {
        List<ExpensesDTO> expenses = expensesService.getExpensesBetweenDates(userId, startMonth, endMonth);
        return excelReportGenerator.generateExpenseReport(expenses);
    }
}
