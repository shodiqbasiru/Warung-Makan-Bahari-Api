package com.enigma.wmb_api.controller.report;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuReportController {
    private final MenuService menuService;

    // csv export
    @Operation(
            summary = "Export menu to CSV",
            description = "Export menu to CSV"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = RouteApi.MENU_PATH + "/export-csv")
    public void exportMenuToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=menus.csv");

        try(PrintWriter writer = response.getWriter()) {
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

    // excel export


}
