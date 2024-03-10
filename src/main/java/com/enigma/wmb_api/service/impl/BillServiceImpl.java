package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.PaginationBillRequest;
import com.enigma.wmb_api.dto.request.UpdateTransactionStatusRequest;
import com.enigma.wmb_api.dto.response.BillDetailResponse;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.response.PaymentResponse;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.BillRepository;
import com.enigma.wmb_api.service.*;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private final ValidationUtil validation;
    private final PaymentService paymentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BillResponse create(BillRequest request) {
        Customer customerId = customerService.getById(request.getCustomerId());

        MTable tableId = request.getTableId().isEmpty() ? null : tableService.getById(request.getTableId());

        List<TransType> transTypes = typeService.getAll();
        TransType transTypeId = tableId != null ? transTypes.get(0) : transTypes.get(1);

        validation.validate(request);

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
                    validation.validate(detailRequest);
                    return BillDetail.builder()
                            .bill(bill)
                            .menu(menuId)
                            .qty(detailRequest.getQty())
                            .price(menuId.getPrice())
                            .build();
                }).toList();
        billDetailService.createBulk(billDetails);
        bill.setBillDetails(billDetails);

        Payment payment = paymentService.createPayment(bill);
        bill.setPayment(payment);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectUrl())
                .transactionStatus(payment.getTransactionStatus())
                .build();

        List<BillDetailResponse> billDetailResponses = billDetails.stream()
                .map(detail -> BillDetailResponse.builder()
                        .id(detail.getId())
                        .menuId(detail.getMenu().getId())
                        .qty(detail.getQty())
                        .price(detail.getPrice())
                        .build())
                .toList();

        String idTable = (bill.getMTable() != null) ? bill.getMTable().getId() : null;

        return BillResponse.builder()
                .id(bill.getId())
                .date(bill.getDate())
                .customerId(bill.getCustomer().getId())
                .tableId(idTable)
                .transType(bill.getTransType().getId().toString())
                .billDetailResponses(billDetailResponses)
                .paymentResponse(paymentResponse)
                .build();
    }

   @Transactional(readOnly = true)
    @Override
    public Page<BillResponse> getAll(PaginationBillRequest request) {
        if (request.getPage() < 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);

        Page<Bill> billPage = billRepository.findAll(pageable);
        List<BillResponse> billResponses = billPage.getContent().stream()
                .map(bill -> {
                    List<BillDetailResponse> billDetailResponses = bill.getBillDetails().stream()
                            .map(detail -> BillDetailResponse.builder()
                                    .id(detail.getId())
                                    .menuId(detail.getMenu().getId())
                                    .qty(detail.getQty())
                                    .price(detail.getPrice())
                                    .build())
                            .toList();

                    String idTable = (bill.getMTable() != null) ? bill.getMTable().getId() : null;
                    return BillResponse.builder()
                            .id(bill.getId())
                            .date(bill.getDate())
                            .customerId(bill.getCustomer().getId())
                            .tableId(idTable)
                            .transType(bill.getTransType().getId().toString())
                            .billDetailResponses(billDetailResponses)
                            .build();
                })
                .toList();
        return new PageImpl<>(billResponses,pageable,billPage.getTotalElements());

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateTransactionStatusRequest request) {
        Bill bill = billRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));

        Payment payment = bill.getPayment();
        payment.setTransactionStatus(request.getTransactionStatus());
    }

}
