package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.response.ResponseHandler;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.CUSTOMER_PATH)
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> create(@RequestBody CustomerRequest request) {
        try {
            Customer result = customerService.create(request);
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
            List<Customer> result = customerService.getAll();
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
            Customer result = customerService.getById(id);
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
    public ResponseEntity<Object> update(@RequestBody Customer request){
        try {
            Customer result = customerService.update(request);
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
            customerService.delete(id);
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
