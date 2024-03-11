package com.enigma.wmb_api.controller.report;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.service.BillService;
import com.itextpdf.kernel.geom.PageSize;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteApi.BILL_PATH)
public class BillReportController {
    private final BillService billService;

    // csv export
    @Operation(
            summary = "Export Bill to CSV",
            description = "Export Bill to CSV"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/export-csv")
    public void exportBillToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=bills.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("orderId,transDate,customerName,tableName,transType,paymentStatus,menuDetails");
            for (Bill bill : billService.getAll()) {
                String tableId = bill.getMTable() != null ? bill.getMTable().getTableName() : null;
                writer.printf("%s,%s,%s,%s,%s,%s,%s\n",
                        bill.getId(),
                        bill.getDate(),
                        bill.getCustomer().getCustomerName(),
                        tableId,
                        bill.getTransType().getDescription(),
                        bill.getPayment().getTransactionStatus(),
                        bill.getBillDetails().stream()
                                .map(detail ->
                                                detail.getMenu().getMenuName() + "," +
                                                detail.getQty() + "x" +
                                                detail.getPrice()
                                ).collect(Collectors.joining(";")));
            }
        }
    }

    // pdf export
    @Operation(
            summary = "Export Bill to PDF",
            description = "Export Bill to PDF"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/export-pdf")
    public void exportBillToPdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bills.pdf");

        // font size
        float fontTitle = 14.0f;
        float fontTable = 12.0f;

        PageSize pageSize = PageSize.A4.rotate();
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(pageSize);
        try (Document document = new Document(pdf)) {
            document.add(new Paragraph("Bill Report").setFontSize(fontTitle).setTextAlignment(TextAlignment.CENTER).setBold());
            document.add(new Paragraph("\n"));

            // Create table columns
            Table table = new Table(UnitValue.createPercentArray(8)).useAllAvailableWidth();

            // table header
            String[] headers = {
                    "Order ID",
                    "Transaction Date",
                    "Customer Name",
                    "Table Name",
                    "Transaction Type",
                    "Payment Status",
                    "Menu Details",
                    "Total Price"
            };

            for (String header : headers) {
                table.addHeaderCell(header).setFontSize(fontTable);
            }

            // table body
            for (Bill bill : billService.getAll()) {
                String tableId = bill.getMTable() != null ? bill.getMTable().getTableName() : "N/A";

                table.addCell(bill.getId()).setFontSize(fontTable);
                table.addCell(String.valueOf(bill.getDate())).setFontSize(fontTable);
                table.addCell(bill.getCustomer().getCustomerName()).setFontSize(fontTable);
                table.addCell(tableId).setFontSize(fontTable);
                table.addCell(bill.getTransType().getDescription()).setFontSize(fontTable);
                table.addCell(bill.getPayment().getTransactionStatus()).setFontSize(fontTable);
                table.addCell(bill.getBillDetails().stream()
                        .map(detail ->
                                detail.getMenu().getMenuName() + ", " +
                                        detail.getQty() + "x" +
                                        detail.getPrice()
                        ).collect(Collectors.joining("\n\n"))
                ).setFontSize(fontTable);
                table.addCell(bill.getBillDetails().stream()
                        .map(detail -> detail.getQty() * detail.getPrice())
                        .reduce(0, Integer::sum)
                        .toString()
                ).setFontSize(fontTable);
            }

            document.add(table);

        }
    }

    // excel export
}
