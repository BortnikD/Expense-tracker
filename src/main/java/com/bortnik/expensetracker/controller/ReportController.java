package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.service.UserService;
import com.bortnik.expensetracker.service.reports.BudgetReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final BudgetReportService budgetReportService;
    private final UserService userService;

    private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @GetMapping("/budget/excel")
    public ResponseEntity<byte[]> getBudgetReportExcel(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate month
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        byte[] reportData = budgetReportService.generateBudgetReport(userId, month);
        String filename = "budget_report_" + month.getMonth() + "_" + month.getYear() + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", EXCEL_CONTENT_TYPE)
                .body(reportData);
    }

    @GetMapping("/expenses/excel")
    public ResponseEntity<byte[]> getExpensesReportExcel(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate startMonth,
            @RequestParam LocalDate endMonth
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        byte[] reportData = budgetReportService.generateExpensesReport(userId, startMonth, endMonth);
        String filename = "expenses_report_" + startMonth.getMonth() + "_" + startMonth.getYear() +
                "_to_" + endMonth.getMonth() + "_" + endMonth.getYear() + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", EXCEL_CONTENT_TYPE)
                .body(reportData);

    }
}
