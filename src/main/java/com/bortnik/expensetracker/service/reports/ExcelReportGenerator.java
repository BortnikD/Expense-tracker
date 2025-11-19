package com.bortnik.expensetracker.service.reports;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetRowData;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.bortnik.expensetracker.service.reports.BaseReportGenerator.BUDGET_HEADERS;
import static com.bortnik.expensetracker.service.reports.BaseReportGenerator.EXPENSE_HEADERS;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExcelReportGenerator {

    private final BaseReportGenerator  baseReportGenerator;

    public byte[] generateBudgetReport(List<BudgetPlanDTO> budgetPlans) {
        log.info("Starting budget report generation for {} plans", budgetPlans.size());

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Budget Report");

            createHeaderRow(sheet, BUDGET_HEADERS, workbook);
            fillBudgetData(sheet, budgetPlans, workbook);
            autoSizeColumns(sheet, BUDGET_HEADERS.length);

            return writeToBytes(workbook);

        } catch (Exception e) {
            log.error("Error generating budget report", e);
            throw new RuntimeException("Failed to generate Excel budget report", e);
        }
    }

    public byte[] generateExpenseReport(List<ExpensesDTO> expenses) {
        log.info("Starting expense report generation for {} expenses", expenses.size());

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Expense Report");

            createHeaderRow(sheet, EXPENSE_HEADERS, workbook);
            fillExpenseData(sheet, expenses);
            addTotalRow(sheet, expenses, expenses.size() + 2);
            autoSizeColumns(sheet, EXPENSE_HEADERS.length);

            return writeToBytes(workbook);

        } catch (Exception e) {
            log.error("Error generating expense report", e);
            throw new RuntimeException("Failed to generate Excel expense report", e);
        }
    }

    private void createHeaderRow(Sheet sheet, String[] headers, Workbook workbook) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillBudgetData(Sheet sheet, List<BudgetPlanDTO> budgetPlans, XSSFWorkbook workbook) {
        int rowNum = 1;

        for (BudgetPlanDTO plan : budgetPlans) {
            Row row = sheet.createRow(rowNum++);
            BudgetRowData data = baseReportGenerator.calculateBudgetData(plan);

            row.createCell(0).setCellValue(baseReportGenerator.formatMonth(plan.getMonth()));
            row.createCell(1).setCellValue(baseReportGenerator.getCategoryName(plan.getCategoryId()));
            row.createCell(2).setCellValue(data.limit());

            createStyledCell(row, 3, data.spent(), createColoredStyle(workbook, data.isOverBudget()));
            createStyledCell(row, 4, data.remaining(), createColoredStyle(workbook, data.isOverBudget()));
            createStyledCell(row, 5, data.percentageDecimal(), createPercentageStyle(workbook, data.isOverBudget()));
        }
    }

    private void fillExpenseData(Sheet sheet, List<ExpensesDTO> expenses) {
        int rowNum = 1;

        for (ExpensesDTO expense : expenses) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(baseReportGenerator.formatDate(expense.getDate()));
            row.createCell(1).setCellValue(baseReportGenerator.getCategoryName(expense.getCategoryId()));
            row.createCell(2).setCellValue(expense.getAmount());
            row.createCell(3).setCellValue(expense.getDescription());
        }
    }

    private void addTotalRow(Sheet sheet, List<ExpensesDTO> expenses, int rowNum) {
        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(0).setCellValue("Total amount spent");

        double total = expenses.stream()
                .mapToDouble(ExpensesDTO::getAmount)
                .sum();

        totalRow.createCell(1).setCellValue(total);
    }

    private void createStyledCell(Row row, int column, double value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private byte[] writeToBytes(XSSFWorkbook workbook) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            log.info("Excel report successfully generated");
            return out.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        setBorders(style);

        return style;
    }

    private CellStyle createColoredStyle(XSSFWorkbook workbook, boolean isOverBudget) {
        CellStyle style = workbook.createCellStyle();

        applyColorScheme(style, workbook, isOverBudget);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.RIGHT);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));

        return style;
    }

    private CellStyle createPercentageStyle(XSSFWorkbook workbook, boolean isOverBudget) {
        CellStyle style = workbook.createCellStyle();

        applyColorScheme(style, workbook, isOverBudget);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.00%"));

        return style;
    }

    private void applyColorScheme(CellStyle style, XSSFWorkbook workbook, boolean isOverBudget) {
        Font font = workbook.createFont();

        if (isOverBudget) {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
            font.setColor(IndexedColors.DARK_RED.getIndex());
            font.setBold(true);
        } else {
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            font.setColor(IndexedColors.DARK_GREEN.getIndex());
        }

        style.setFont(font);
    }

    private void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}