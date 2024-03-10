package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.PaginationBillRequest;
import com.enigma.wmb_api.dto.request.UpdateTransactionStatusRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.entity.Bill;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BillService {
    BillResponse create(BillRequest request);
    Page<BillResponse> getAll(PaginationBillRequest request);
    List<Bill> getAll();
    void updateStatus(UpdateTransactionStatusRequest request);
}
