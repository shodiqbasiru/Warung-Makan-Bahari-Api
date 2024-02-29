package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.TRANS_TYPE_PATH)
public class TransTypeController {
    private final TransTypeService typeService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<TransType>>> getAll() {
            List<TransType> result = typeService.getAll();
            CommonResponse<List<TransType>> response = CommonResponse.<List<TransType>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Get all data successfully")
                    .data(result)
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TransType>> findById(@PathVariable String id){
            TransType result = typeService.getById(id);
            CommonResponse<TransType> response = CommonResponse.<TransType>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Get data successfully")
                    .data(result)
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TransType>> update(@RequestBody TransType transType){
            TransType result = typeService.update(transType);
            CommonResponse<TransType> response = CommonResponse.<TransType>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Updates data successfully")
                    .data(result)
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
