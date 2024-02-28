package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.response.BillDetailResponse;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.BillRepository;
import com.enigma.wmb_api.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailService billDetailService;
    private final CustomerService customerService;
    private final TableService tableService;
    private final TransTypeService typeService;
    private final MenuService menuService;
    @Transactional
    @Override
    public BillResponse create(BillRequest request) {
        Customer customerId = customerService.getById(request.getCustomerId());
        MTable tableId = tableService.getById(request.getTableId());
        TransType transTypeId = typeService.getById(request.getTransTypeId());


        Bill bill = Bill.builder()
                .date(new Date())
                .customer(customerId)
                .mTable(tableId)
                .transType(transTypeId)
                .build();
        billRepository.saveAndFlush(bill);

        List<BillDetail> billDetails = request.getBillDetailRequests().stream()

                .map(detailRequest -> {
                    Menu menuId = menuService.getById(detailRequest.getMenuId());

                    return BillDetail.builder()
                            .bill(bill)
                            .menu(menuId)
                            .qty(detailRequest.getQty())
                            .price(menuId.getPrice())
                            .build();
                }).toList();
        billDetailService.createBulk(billDetails);
        bill.setBillDetails(billDetails);
        return getBillResponse(billDetails,bill);
    }

    @Override
    public List<BillResponse> getAll() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream().map(bill -> getBillResponse(bill.getBillDetails(),bill)).toList();
    }

    private static BillResponse getBillResponse(List<BillDetail> billDetails, Bill bill) {
        List<BillDetailResponse> billDetailResponses = billDetails.stream()
                .map(detail -> BillDetailResponse.builder()
                        .id(detail.getId())
                        .menuId(detail.getMenu().getId())
                        .qty(detail.getQty())
                        .price(detail.getPrice())
                        .build())
                .toList();

        return BillResponse.builder()
                .id(bill.getId())
                .date(bill.getDate())
                .customerId(bill.getCustomer().getId())
                .tableName(bill.getMTable().getTableName())
                .transType(bill.getTransType().getId())
                .billDetailResponses(billDetailResponses)
                .build();
    }
}
