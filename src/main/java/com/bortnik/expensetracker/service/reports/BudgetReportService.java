package com.bortnik.expensetracker.service.reports;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.service.BudgetPlanService;
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

    public byte[] generateReport(UUID userId, LocalDate month) {
        List<BudgetPlanDTO> budgetPlans = budgetPlanService.getBudgetPlansByUserIdAndMonth(userId, month);
        return excelReportGenerator.generateReport(budgetPlans);
    }
}
