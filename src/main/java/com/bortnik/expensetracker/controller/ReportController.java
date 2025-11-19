package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.service.UserService;
import com.bortnik.expensetracker.service.reports.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    private final ReportService budgetReportService;
    private final UserService userService;

    private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @GetMapping("/budget/excel")
    public ResponseEntity<byte[]> getBudgetReportExcel(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate month
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        byte[] reportData = budgetReportService.generateBudgetExcelReport(userId, month);
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
        byte[] reportData = budgetReportService.generateExpensesExcelReport(userId, startMonth, endMonth);
        String filename = "expenses_report_" + startMonth.getMonth() + "_" + startMonth.getYear() +
                "_to_" + endMonth.getMonth() + "_" + endMonth.getYear() + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", EXCEL_CONTENT_TYPE)
                .body(reportData);

    }

    @GetMapping("/budget/pdf")
    public ResponseEntity<byte[]> getExpensesReportPdf(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate month
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        byte[] reportData = budgetReportService.generateBudgetPdfReport(userId, month);
        String filename = "budget_report_" + month.getMonth() + "_" + month.getYear() + ".pdf";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
                .body(reportData);

    }

    @GetMapping("/expenses/pdf")
    public ResponseEntity<byte[]> getExpensesReportPdf(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate startMonth,
            @RequestParam LocalDate endMonth
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        byte[] reportData = budgetReportService.generateExpensesPdfReport(userId, startMonth, endMonth);
        String filename = "expenses_report_" + startMonth.getMonth() + "_" + startMonth.getYear() +
                "_to_" + endMonth.getMonth() + "_" + endMonth.getYear() + ".pdf";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
                .body(reportData);

    }
}
