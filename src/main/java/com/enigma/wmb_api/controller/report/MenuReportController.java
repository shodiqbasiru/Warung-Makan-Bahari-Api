package com.enigma.wmb_api.controller.report;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteApi.MENU_PATH)
public class MenuReportController {
    private final MenuService menuService;

    // csv export
    @Operation(
            summary = "Export menu to CSV",
            description = "Export menu to CSV"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/export-csv")
    public void exportMenuToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=menus.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Menu Id,Menu Name,Price");
            menuService.getAll().forEach(menu ->
                    writer.printf("%s,%s,%s%n",
                            menu.getId(),
                            menu.getMenuName(),
                            menu.getPrice()
                    )
            );
        }
    }

    // pdf export
    @Operation(
            summary = "Export menu to PDF",
            description = "Export menu to PDF"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/export-pdf")
    public void exportMenuToPdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=menus.pdf");

        // font size
        float fonTitle = 14.0f;
        float fontTable = 12.0f;

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        try (Document document = new Document(pdf)) {
            document.add(new Paragraph("Menu List").setFontSize(fonTitle).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            // Create Table Columns
            Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            table.addHeaderCell("Menu Id");
            table.addHeaderCell("Menu Name");
            table.addHeaderCell("Price");

            // Create Table Rows
            List<Menu> menus = menuService.getAll();
            menus.forEach(menu -> {
                table.addCell(menu.getId()).setFontSize(fontTable);
                table.addCell(menu.getMenuName()).setFontSize(fontTable);
                table.addCell(menu.getPrice().toString()).setFontSize(fontTable);
            });

            document.add(table);
        }
    }

    // excel export


}
