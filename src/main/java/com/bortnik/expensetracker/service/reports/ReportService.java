package com.bortnik.expensetracker.service.reports;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import com.bortnik.expensetracker.service.BudgetPlanService;
import com.bortnik.expensetracker.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExcelReportGenerator excelReportGenerator;
    private final PdfReportGenerator pdfReportGenerator;
    private final BudgetPlanService budgetPlanService;
    private final ExpensesService expensesService;

    public byte[] generateBudgetExcelReport(
            final UUID userId,
            final LocalDate month
    ) {
        final List<BudgetPlanDTO> budgetPlans = getBudgetPlansBetweenDatesList(userId, month);
        return excelReportGenerator.generateBudgetReport(budgetPlans);
    }

    public byte[] generateExpensesExcelReport(
            final UUID userId,
            final LocalDate startMonth,
            final LocalDate endMonth
    ) {
        final List<ExpensesDTO> expenses = getExpensesBetweenDatesList(userId, startMonth, endMonth);
        return excelReportGenerator.generateExpenseReport(expenses);
    }

    public byte[] generateBudgetPdfReport(
            final UUID userId,
            final LocalDate month
    ) {
        final List<BudgetPlanDTO> budgetPlans = getBudgetPlansBetweenDatesList(userId, month);
        return pdfReportGenerator.generateBudgetReport(budgetPlans);
    }

    public byte[] generateExpensesPdfReport(
            final UUID userId,
            final LocalDate startMonth,
            final LocalDate endMonth
    ) {
        final List<ExpensesDTO> expenses = getExpensesBetweenDatesList(userId, startMonth, endMonth);
        return pdfReportGenerator.generateExpenseReport(expenses);
    }

    private List<ExpensesDTO> getExpensesBetweenDatesList(
            final UUID userId,
            final LocalDate startMonth,
            final LocalDate endMonth
    ) {
        int pageNumber = 0;
        final int pageSize = 200;
        final List<ExpensesDTO> expenses = new ArrayList<>();
        Page<ExpensesDTO> expensesPage;
        do {
            final Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
            expensesPage = expensesService.getExpensesBetweenDates(userId, startMonth, endMonth, pageable);
            expenses.addAll(expensesPage.getContent());
            pageNumber++;
        } while (expensesPage.hasNext());
        return expenses;
    }

    private List<BudgetPlanDTO> getBudgetPlansBetweenDatesList(
            final UUID userId,
            final LocalDate month
    ) {
        int pageNumber = 0;
        final int pageSize = 200;
        final List<BudgetPlanDTO> expenses = new ArrayList<>();
        Page<BudgetPlanDTO> expensesPage;
        do {
            final Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
            expensesPage = budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month, pageable);
            expenses.addAll(expensesPage.getContent());
            pageNumber++;
        } while (expensesPage.hasNext());
        return expenses;
    }
}
