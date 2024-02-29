package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.PaginationBillRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PaginationResponse;
import com.enigma.wmb_api.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.BILL_PATH)
public class BillController {
    private final BillService billService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<BillResponse>> create(@RequestBody BillRequest request) {
        BillResponse result = billService.create(request);
        CommonResponse<BillResponse> response = CommonResponse.<BillResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created a new transaction successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<BillResponse>>> getAllBills(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy",defaultValue = "date") String sortBy,
            @RequestParam(name = "direction",defaultValue = "asc") String direction
    ) {
        PaginationBillRequest billRequest = PaginationBillRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();
        Page<BillResponse> result = billService.getAll(billRequest);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .page(result.getPageable().getPageNumber() + 1)
                .size(result.getPageable().getPageSize())
                .hasNext(result.hasNext())
                .hasPrevious(result.hasPrevious())
                .build();

        CommonResponse<List<BillResponse>> responses = CommonResponse.<List<BillResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all transaction successfully")
                .data(result.getContent())
                .pages(paginationResponse)
                .build();
        return new ResponseEntity<>(responses,HttpStatus.OK);
    }
}
