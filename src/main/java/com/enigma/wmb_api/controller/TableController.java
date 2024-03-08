package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.entity.MTable;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.TABLE_PATH)
public class TableController {
    private final TableService tableService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MTable>> create(@RequestBody TableRequest request) {
        MTable result = tableService.create(request);
        CommonResponse<MTable> response = CommonResponse.<MTable>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created a new table successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<MTable>>> getAll() {
        List<MTable> result = tableService.getAll();
        CommonResponse<List<MTable>> responses = CommonResponse.<List<MTable>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all data successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(responses,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MTable>> findById(@PathVariable String id) {
        MTable result = tableService.getById(id);
        CommonResponse<MTable> response = CommonResponse.<MTable>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get data successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MTable>> update(@RequestBody MTable request) {
        MTable result = tableService.update(request);
        CommonResponse<MTable> response = CommonResponse.<MTable>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Updated data successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<CommonResponse<MTable>> delete(@PathVariable String id) {
        tableService.delete(id);
        CommonResponse<MTable> response = CommonResponse.<MTable>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Deleted data successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
