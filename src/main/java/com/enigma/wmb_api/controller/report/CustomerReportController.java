package com.enigma.wmb_api.controller.report;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequiredArgsConstructor
public class CustomerReportController {
    private final CustomerService customerService;

    // csv export
    @GetMapping(path = RouteApi.CUSTOMER_PATH + "/export-csv")
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

    // excel export
}
