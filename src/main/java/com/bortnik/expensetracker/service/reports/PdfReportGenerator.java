package com.bortnik.expensetracker.service.reports;

import com.bortnik.expensetracker.dto.budget.BudgetPlanDTO;
import com.bortnik.expensetracker.dto.budget.BudgetRowData;
import com.bortnik.expensetracker.dto.expenses.ExpensesDTO;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.bortnik.expensetracker.service.reports.BaseReportGenerator.BUDGET_HEADERS;
import static com.bortnik.expensetracker.service.reports.BaseReportGenerator.EXPENSE_HEADERS;

@Component
@Slf4j
@RequiredArgsConstructor
public class PdfReportGenerator {

    private final BaseReportGenerator baseReportGenerator;

    private static final Color HEADER_BG_COLOR = new DeviceRgb(200, 200, 200);
    private static final Color OVER_BUDGET_BG_COLOR = new DeviceRgb(255, 200, 200);
    private static final Color OVER_BUDGET_TEXT_COLOR = new DeviceRgb(139, 0, 0);
    private static final Color UNDER_BUDGET_BG_COLOR = new DeviceRgb(200, 255, 200);
    private static final Color UNDER_BUDGET_TEXT_COLOR = new DeviceRgb(0, 100, 0);


    public byte[] generateBudgetReport(List<BudgetPlanDTO> plans) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            addTitle(document, "Budget Report");
            addBudgetTable(document, plans);

            document.close();
            log.info("PDF budget report generated successfully. Size: {} bytes", out.size());

        } catch (Exception e) {
            log.error("Error generating PDF budget report", e);
            throw new RuntimeException("Failed to generate PDF budget report", e);
        }

        return out.toByteArray();
    }

    public byte[] generateExpenseReport(List<ExpensesDTO> expenses) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            addTitle(document, "Expense Report");
            addExpenseTable(document, expenses);
            addExpenseTotal(document, expenses);

            document.close();
            log.info("PDF expense report generated successfully. Size: {} bytes", out.size());

        } catch (Exception e) {
            log.error("Error generating PDF expense report", e);
            throw new RuntimeException("Failed to generate PDF expense report", e);
        }

        return out.toByteArray();
    }

    private void addTitle(Document document, String title) {
        document.add(new Paragraph(title)
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));
    }

    private void addBudgetTable(Document document, List<BudgetPlanDTO> plans) {
        float[] columnWidths = {2, 2, 1.5f, 1.5f, 1.5f, 1};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        for (String header : BUDGET_HEADERS) {
            table.addHeaderCell(createHeaderCell(header));
        }

        for (BudgetPlanDTO plan : plans) {
            try {
                BudgetRowData data = baseReportGenerator.calculateBudgetData(plan);
                Color bgColor = data.isOverBudget() ? OVER_BUDGET_BG_COLOR : UNDER_BUDGET_BG_COLOR;
                Color textColor = data.isOverBudget() ? OVER_BUDGET_TEXT_COLOR : UNDER_BUDGET_TEXT_COLOR;

                table.addCell(createCell(baseReportGenerator.formatMonth(plan.getMonth())));
                table.addCell(createCell(baseReportGenerator.getCategoryName(plan.getCategoryId())));

                table.addCell(createNumericCell(String.format("%.2f", data.limit()), bgColor, textColor));
                table.addCell(createNumericCell(String.format("%.2f", data.spent()), bgColor, textColor));
                table.addCell(createNumericCell(String.format("%.2f", data.remaining()), bgColor, textColor));
                table.addCell(createNumericCell(String.format("%.1f%%", data.percentageDecimal() * 100), bgColor, textColor));

            } catch (Exception e) {
                log.error("Error processing budget plan: {}", plan, e);
            }
        }

        document.add(table);
    }

    private void addExpenseTable(Document document, List<ExpensesDTO> expenses) {
        float[] columnWidths = {2, 2, 1.5f, 3};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        for (String header : EXPENSE_HEADERS) {
            table.addHeaderCell(createHeaderCell(header));
        }

        for (ExpensesDTO expense : expenses) {
            table.addCell(createCell(baseReportGenerator.formatDate(expense.getDate())));
            table.addCell(createCell(baseReportGenerator.getCategoryName(expense.getCategoryId())));
            table.addCell(createNumericCell(String.format("%.2f", expense.getAmount())));
            table.addCell(createCell(expense.getDescription()));
        }

        document.add(table);
    }

    private void addExpenseTotal(Document document, List<ExpensesDTO> expenses) {
        double total = expenses.stream()
                .mapToDouble(ExpensesDTO::getAmount)
                .sum();

        document.add(new Paragraph(String.format("Total Amount Spent: %.2f", total))
                .setFontSize(14)
                .setMarginTop(20)
                .setTextAlignment(TextAlignment.RIGHT));
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setBackgroundColor(HEADER_BG_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5);
    }

    private Cell createCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setPadding(5);
    }

    private Cell createNumericCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5);
    }

    private Cell createNumericCell(String text, Color bgColor, Color textColor) {
        return new Cell()
                .add(new Paragraph(text).setFontColor(textColor))
                .setBackgroundColor(bgColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5);
    }
}