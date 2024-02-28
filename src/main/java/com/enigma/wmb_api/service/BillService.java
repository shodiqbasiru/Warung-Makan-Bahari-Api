package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.response.BillResponse;

import java.util.List;

public interface BillService {
    BillResponse create(BillRequest request);
    List<BillResponse> getAll();
}
