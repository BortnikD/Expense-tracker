package com.bortnik.expensetracker.service.reports;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class ExcelReportGenerator {

    public byte[] generateReport(List<BudgetPlanDTO> budgetPlans) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Budget Report");

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Month", "Category", "Limit", "Spent", "Remaining", "%"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // Data
            int rowNum = 1;
            for (BudgetPlanDTO plan : budgetPlans) {
                Row row = sheet.createRow(rowNum++);
                double limit = plan.getLimitAmount();
                double spent = plan.getSpentAmount();
                double remaining = limit - spent;
                double percentage = limit > 0 ? (spent / limit) * 100 : 0;
                boolean isOverBudget = spent > limit;
                LocalDate month = plan.getMonth();
                String monthStr = month.getMonth().toString() + " " + month.getYear();

                row.createCell(0).setCellValue(monthStr);
                row.createCell(1).setCellValue(String.valueOf(plan.getCategoryId()));

                row.createCell(2).setCellValue(limit);

                Cell spentCell = row.createCell(3);
                spentCell.setCellValue(spent);
                spentCell.setCellStyle(createColoredStyle(workbook, isOverBudget));

                Cell remainingCell = row.createCell(4);
                remainingCell.setCellValue(remaining);
                remainingCell.setCellStyle(createColoredStyle(workbook, isOverBudget));

                Cell percentageCell = row.createCell(5);
                percentageCell.setCellValue(percentage / 100);
                percentageCell.setCellStyle(createPercentageStyle(workbook, isOverBudget));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            log.info("Excel report generation completed");
            return out.toByteArray();

        } catch (Exception e) {
            log.error("Error while generating excel report", e);
            return new byte[0];
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
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createColoredStyle(XSSFWorkbook workbook, boolean isOverBudget) {
        CellStyle style = workbook.createCellStyle();

        setColor(style, isOverBudget, workbook);

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.RIGHT);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));

        return style;
    }

    private CellStyle createPercentageStyle(XSSFWorkbook workbook, boolean isOverBudget) {
        CellStyle style = workbook.createCellStyle();

        setColor(style, isOverBudget, workbook);

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.00%"));

        return style;
    }

    private void setColor(CellStyle style, boolean isOverBudget, XSSFWorkbook workbook) {
        if (isOverBudget) {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
            Font font = workbook.createFont();
            font.setColor(IndexedColors.DARK_RED.getIndex());
            font.setBold(true);
            style.setFont(font);
        } else {
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            Font font = workbook.createFont();
            font.setColor(IndexedColors.DARK_GREEN.getIndex());
            style.setFont(font);
        }
    }
}