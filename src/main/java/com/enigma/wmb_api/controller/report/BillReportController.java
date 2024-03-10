package com.enigma.wmb_api.controller.report;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.service.BillService;
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
    @GetMapping(path = "/export-csv")
    public void exportBillToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment; filename=bills.csv");
        try(PrintWriter writer = response.getWriter()){
            writer.println("orderId,transDate,tableId,transType,paymentStatus,menuDetails");
            billService.getAll().forEach(bill -> {
                String menuDetails = bill.getBillDetails().stream()
                        .map(billDetail -> String.format("%s,%s,%d",
                                billDetail.getMenu().getId(),
                                billDetail.getMenu().getMenuName(),
                                billDetail.getQty()))
                        .collect(Collectors.joining("; "));
                writer.printf("%s,%s,%s,%s,%s,\"%s\"\n",
                        bill.getId(),
                        bill.getDate(),
                        bill.getMTable().getId(),
                        bill.getTransType().getId(),
                        bill.getPayment().getTransactionStatus(),
                        menuDetails
                );
            });
        }

    }

    // pdf export

    // excel export
}
