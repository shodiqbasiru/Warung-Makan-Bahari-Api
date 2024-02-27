package com.enigma.wmb_api.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus httpStatus, Object object) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("message", message);
        map.put("status", httpStatus);
        map.put("data", object);

        return new ResponseEntity<>(map, httpStatus);
    }
}
