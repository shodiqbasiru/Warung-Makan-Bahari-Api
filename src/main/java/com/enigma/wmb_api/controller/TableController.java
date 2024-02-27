package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.ResponseHandler;
import com.enigma.wmb_api.entity.MTable;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.TABLE_PATH)
public class TableController {
    private final TableService tableService;
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> create(@RequestBody TableRequest request) {
        try {
            MTable result = tableService.create(request);
            return ResponseHandler.generateResponse(
                    "SUCCESS",
                    HttpStatus.CREATED,
                    result
            );
        } catch (Exception e) {
            return ResponseHandler.generateResponse(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getAll() {
        try {
            List<MTable> result = tableService.getAll();
            return ResponseHandler.generateResponse(
                    "SUCCESS",
                    HttpStatus.OK,
                    result
            );
        } catch (Exception e) {
            return ResponseHandler.generateResponse(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> findById(@PathVariable String id){
        try {
            MTable result = tableService.getById(id);
            return ResponseHandler.generateResponse(
                    "SUCCESS",
                    HttpStatus.OK,
                    result
            );
        } catch (Exception e) {
            return ResponseHandler.generateResponse(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> update(@RequestBody MTable request){
        try {
            MTable result = tableService.update(request);
            return ResponseHandler.generateResponse(
                    "SUCCESS",
                    HttpStatus.OK,
                    result
            );
        } catch (Exception e) {
            return ResponseHandler.generateResponse(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<Object> delete(@PathVariable String id){
        try {
            tableService.delete(id);
            return ResponseHandler.generateResponse(
                    "SUCCESS",
                    HttpStatus.OK,
                    "ok"
            );
        } catch (Exception e) {
            return ResponseHandler.generateResponse(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }
}
