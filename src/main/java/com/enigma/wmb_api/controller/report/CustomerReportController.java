package com.enigma.wmb_api.controller.report;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.service.CustomerService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteApi.CUSTOMER_PATH)
public class CustomerReportController {
    private final CustomerService customerService;

    // csv export
    @Operation(
            summary = "Export Customer to CSV",
            description = "Export Customer to CSV"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/export-csv")
    public void exportCustomerToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=customers.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Customer Id,Full Name,Email,Phone");
            customerService.getAll().forEach(customer ->
                    writer.printf("%s,%s,%s,%s%n",
                            customer.getId(),
                            customer.getCustomerName(),
                            customer.getUserAccount().getEmail(),
                            customer.getPhoneNumber()
                    )
            );
        }
    }


    // pdf export
    @Operation(
            summary = "Export Customer to PDF",
            description = "Export Customer to PDF"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/export-pdf")
    public void exportCustomerToPdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=customers.pdf");

        // font size
        float fonTitle = 14.0f;
        float fontTable = 12.0f;

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        try (Document document = new Document(pdf)){
            document.add(new Paragraph("Customer List").setFontSize(fonTitle).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            // Create Table Columns
            Table table = new Table(UnitValue.createPercentArray(4));

            String[] headers = {"Customer Id", "Full Name", "Email", "Phone"};
            for (String header : headers) {
                table.addHeaderCell(header);
            }

            // Create Table body
            customerService.getAll().forEach(customer -> {
                if (customer.getCustomerName().contains("admin")) return;

                table.addCell(customer.getId()).setFontSize(fontTable);
                table.addCell(customer.getCustomerName()).setFontSize(fontTable);
                table.addCell(customer.getUserAccount().getEmail()).setFontSize(fontTable);
                table.addCell(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "N/A" ).setFontSize(fontTable);
            });

            document.add(table);
        }
    }

    // excel export
}
